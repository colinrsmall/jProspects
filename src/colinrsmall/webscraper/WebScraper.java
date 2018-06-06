package colinrsmall.webscraper;

import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WebScraper {

    private final int WHL_2017_START = 1014621;
    private final int WHL_2017_END = 1015412;

    private static MutableMap<String, Player> playersMap = Maps.mutable.empty();

    private static WebDriver gamePageDriver;
    private static WebDriver playerPageDriver;

    public static void main(String[] args)
    {
        System.setProperty("webdriver.chrome.driver", "/Users/colinrsmall/Documents/GitHub/jProspects/src/colinrsmall/chromedriver/chromedriver");
        scrape("OHL", 22381, 22383);
    }

    private static void scrape( String league, int firstGame, int lastGame )
    {
        gamePageDriver = new ChromeDriver();
        playerPageDriver = new ChromeDriver();
        String urlBase = getURLBase(league);
        playersMap = playersMap.newEmpty();

        gameLoop:
        for( int i = firstGame; i <= lastGame; i++ ) {
            // Skip a bunch of unused game codes in the OHL
            if (league.equals("OHL") && (26597 < i && i > 999999))
                continue;

            String gameURL = urlBase + "/gamecentre/" + i + "/boxscore";

            String seasonName;
            String homeTeamName;
            String awayTeamName;
            int gameNumber;
            Elements awaySkaters;
            Elements homeSkaters;
            Document parsedHTML;

            // Try five times to get player stats
            for (int openAttempts = 0; true; openAttempts++) {
                gamePageDriver.get(gameURL);
                String innerHTML = gamePageDriver.getPageSource();
                parsedHTML = Jsoup.parse(innerHTML);
                awaySkaters = parsedHTML.select("tbody[data-reactid=\".0.0.3.0.2.0.1.2.0.1\"] .table__tr--dark");
                homeSkaters = parsedHTML.select("tbody[data-reactid=\".0.0.3.0.2.1.1.2.0.1\"] .table__tr--dark");
                if (awaySkaters.size() != 0 || homeSkaters.size() != 0)
                    break;
                if (openAttempts > 5)
                    continue gameLoop;
            }

            seasonName = consolidateSeasonName(parsedHTML.select("[data-reactid=\".0.0.0.3\"]").get(0).ownText(), league);
            gameNumber = Integer.parseInt(parsedHTML.select("[data-reactid=\".0.0.0.3\"]").get(0).ownText().split(" ")[2]);
            homeTeamName = parsedHTML.select("[data-reactid=\".0.0.0.2\"]").get(0).attr("class").split(" ")[1].split("-")[3];
            awayTeamName = parsedHTML.select("[data-reactid=\".0.0.0.0\"]").get(0).attr("class").split(" ")[1].split("-")[3];

            // Save players in the game to memory if they don't already exist
            for( Element playerElement : homeSkaters)
            {
                playerBuilder(playerElement, league);
            }

            // Saves all goals in the game to their respective players
            Element goalsTable = parsedHTML.selectFirst("[data-reactid=\".0.0.3.0.7\"]");
            for( Element goalElement : goalsTable.children() )
            {
                goalBuilder(goalElement, seasonName, gameNumber, homeTeamName, awayTeamName);
            }

            // Saves all penalties in the game to their respective players
            Element penaltyTable = parsedHTML.selectFirst("[data-reactid=\".0.0.3.0.8\"]");
            for( Element penaltyElement : penaltyTable.children())
            {
                penaltyBuilder(penaltyElement, seasonName, gameNumber, homeTeamName, awayTeamName);
            }
        }

    }

    private static void penaltyBuilder(Element penaltyElement, String seasonName, int gameNumber, String homeTeamName, String awayTeamName)
    {
        //period, subject, teamFor, teamAgainst, seasonName, gameNumber, minutes, offsetting, major, misconduct, gameInfraction
        int period;
        Player penaltyTaker;
        String teamFor;
        String teamAgainst;
        int minutes;
        Boolean offsetting = true;
        String penaltyType;
        String penaltyName;

        period = Character.getNumericValue(penaltyElement.select("div").get(2).ownText().charAt(0));
        String penaltyTakerName = nameFixer(penaltyElement.selectFirst("a").ownText());
        penaltyTaker = playersMap.get(penaltyTakerName);
        teamFor = penaltyElement.selectFirst("div").attr("class").split(" ")[1].split("-")[3];
        teamAgainst = teamFor.equals(homeTeamName) ? awayTeamName : homeTeamName;
        Element infoBox = penaltyElement.select("div").get(3);
        penaltyName = infoBox.selectFirst("span").select("span").get(1).ownText();
        minutes = Integer.parseInt(infoBox.select("span").get(1).select("span").get(1).ownText().split(":")[0]);
        penaltyType = infoBox.select("span").get(1).select("span").get(0).ownText();
        Elements flags = penaltyElement.select("span");
        for( Element flag : flags )
        {
            switch(flag.ownText())
            {
                case "Power Play": offsetting = false;
            }
        }
        penaltyTaker.addPenalty(new Penalty(period, penaltyTaker, teamFor, teamAgainst, seasonName, gameNumber, minutes, offsetting, penaltyType, penaltyName));
    }

    private static void playerBuilder( Element playerElement, String league )
    {
        Elements cells = playerElement.select("td");
        String position = cells.get(0).ownText();
        Element nameElement = cells.get(2).select("a").get(0);
        String name = nameElement.ownText();
        Player player = playersMap.get(name);
        if( player == null )
        {
            BirthdayDraftStatusContainer birthdayDraftStatusContainer = getBirthdayAndDraftStatus(league, nameElement.attr("href"));
            String firstName = name.split(" ")[1];
            String lastName = name.split(" ")[0].split(",")[0];
            player = new Player(firstName, lastName, position, birthdayDraftStatusContainer);
            playersMap.put(name, player);
        }
    }

    private static String nameFixer( String name )
    {
        String[] nameParts = name.split(" ");
        return nameParts[1] + ", " + nameParts[0];
    }

    private static void goalBuilder( Element goalElement, String seasonName, int gameNumber, String homeTeamName, String awayTeamName)
    {
        // period, subject, teamFor, teamAgainst, season, gameNumber, primary assister, secondary assister
        int period;
        Player scorer;
        String teamFor;
        String teamAgainst;
        Player primAssister;
        Player secAssister;
        GameState gameState = GameState.EVEN_STRENGTH;
        Boolean emptyNet = false;
        Boolean insurance = false;
        Boolean gameWinning = false;

        period = Character.getNumericValue(goalElement.select("div").get(2).ownText().charAt(0));
        String scorerName = nameFixer(goalElement.select("div").get(3).selectFirst("div").selectFirst("a").ownText());
        scorer = playersMap.get(scorerName);
        teamFor = goalElement.selectFirst("div").attr("class").split(" ")[1].split("-")[3];
        teamAgainst = teamFor.equals(homeTeamName) ? awayTeamName : homeTeamName;
        // Try to select the name of the primary and secondary assisters, exception thrown if they don't exist, so set player to null
        try{String primAssisterName = nameFixer(goalElement.select("span").get(3).selectFirst("a").ownText());
            primAssister = playersMap.get(primAssisterName);}
        catch(IndexOutOfBoundsException e){primAssister = null;}
        try{String secAssisterName = nameFixer(goalElement.select("span").get(4).selectFirst("a").ownText());
            secAssister = playersMap.get(secAssisterName);}
        catch(IndexOutOfBoundsException e){secAssister = null;}
        // Look for certain flags for the goal
        Elements flags = goalElement.select("span");
        for( Element flag : flags)
        {
            switch( flag.text() ){
                case "Power Play": gameState = GameState.POWERPLAY; break;
                case "Short Handed": gameState = GameState.SHORT_HANDED; break;
                case "Insurance Goal": insurance = true; break;
                case "Empty Net": emptyNet = true; break;
                case "Game Winning": gameWinning = true; break;
            }
        }
        scorer.addGoal(new Goal(period, scorer, teamFor, teamAgainst, seasonName, gameNumber, primAssister, secAssister, emptyNet, gameWinning, insurance, gameState));
        if(primAssister != null)
            primAssister.addAssist(new Assist(period, primAssister, teamFor, teamAgainst, seasonName, gameNumber, scorer, secAssister, emptyNet, gameWinning, insurance, true, gameState));
        if(primAssister != null)
            primAssister.addAssist(new Assist(period, secAssister, teamFor, teamAgainst, seasonName, gameNumber, scorer, primAssister, emptyNet, gameWinning, insurance, false, gameState));
    }

    private static String getURLBase( String league )
    {
        switch( league )
        {
            case "OHL": return "http://www.ontariohockeyleague.com";
            case "WHL": return "http://www.whl.ca";
            case "QMJHL": return "http://www.theqmjhl.ca";
            //default: throw new Exception("Invalid league name: " + league + " is not one of OHL, WHL, QMJHL");
        }
        return ""; //Will throw exception in Selenium if nothing is returned, but this shouldn't happen
    }

    private static String consolidateSeasonName(String season, String league)
    {
        switch (league){
            case "OHL": return season.split(" - ")[1].split(" ")[0];
            case "WHL": String[] temp =  season.split(" - ")[1].split(" ");
                        return temp[0] + "-" + temp[2];
            case "QMJHL": return season.split(" - ")[1].split(" | ")[0];
            default: return "";
        }
    }

    private static BirthdayDraftStatusContainer getBirthdayAndDraftStatus( String league, String href)
    {
        BirthdayDraftStatusContainer container = new BirthdayDraftStatusContainer();

        String url = getURLBase(league) + href;
        playerPageDriver.get(url);

        String innerHTML = gamePageDriver.getPageSource();
        Document parsedHTML = Jsoup.parse(innerHTML);

        LocalDate birthday;
        DraftStatusContainer draftStatus;

        if (league.equals("QMJHL"))
        {
            // Get the player's birthday
            try {
                Element qmjhlPlayerPanel = parsedHTML.selectFirst(".info-con-table01");
                Element row = qmjhlPlayerPanel.select("tr").get(2);
                String birthdayText = row.selectFirst("span").ownText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                birthday = LocalDate.parse(birthdayText, formatter);
            }
            catch( IndexOutOfBoundsException e )
            {
                birthday = LocalDate.MIN;
            }
            // Get the player's draft status
            try{
                Element qmjhlDraftPanel = parsedHTML.selectFirst(".info-con-table02");
                Element row = qmjhlDraftPanel.selectFirst("td");
                String statusText = row.ownText();
                if( !statusText.contains("NHL"))
                    draftStatus = new DraftStatusContainer("Undrafted", league);
                else
                    draftStatus = new DraftStatusContainer(statusText, league);
            }
            catch ( IndexOutOfBoundsException e )
            {
                draftStatus = new DraftStatusContainer("Undrafted", league);
            }
            return new BirthdayDraftStatusContainer(birthday, draftStatus);
        }
        else{
            System.out.println(parsedHTML);
            String birthdayText = parsedHTML.selectFirst("[data-reactid=\".0.0.0.0.2.3.1\"]").ownText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            birthday = LocalDate.parse(birthdayText, formatter);
            try{
                Element draftTable = parsedHTML.selectFirst("[data-reactid=\".0.0.0.0.2.6.1\"]");
                String statusText = draftTable.selectFirst("div").ownText();
                if( !statusText.contains("NHL"))
                    draftStatus = new DraftStatusContainer("Undrafted", league);
                else
                    draftStatus = new DraftStatusContainer(statusText, league);
            }
            catch( IndexOutOfBoundsException e )
            {
                draftStatus = new DraftStatusContainer("Undrafted", league);
            }
            return new BirthdayDraftStatusContainer(birthday, draftStatus);
        }
    }

}
