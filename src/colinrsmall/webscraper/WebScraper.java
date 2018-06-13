package colinrsmall.webscraper;

import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScraper {

    private static final int WHL_2017_START = 1014621;
    private static final int WHL_2017_END = 1015412;
    private static final int WHL_2008_END = 1004776;
    private static final int QMJHL_2017_START = 25784;
    private static final int QMJHL_2018_END = 26127;
    //WHL - After 1013620 the season notation changes
    //WHL - After 24644 the date line changes

    private static String outputFilePath = "out/csv/";

    private static MutableMap<String, Player> playersMap = Maps.mutable.empty();
    private static ArrayList<String[]> gamesList;

    private static WebDriver playerPageDriver;

    public static void main(String[] args)
    {
        System.setProperty("webdriver.chrome.driver", "/Users/colinrsmall/Documents/GitHub/jProspects/src/colinrsmall/chromedriver/chromedriver");
        //scrape("QMJHL", 5213, QMJHL_2018_END);
        scrape("OHL", 6147, 23134);
        exportGoalsCSV();
        exportAssistsCSV();
        exportPenaltiesCSV();
        exportPlayersCSV();
        exportGamesCSV();
        System.out.println("DONE");
    }

    private static void exportGamesCSV()
    {
        PrintWriter writer;
        try {
            File out = new File(outputFilePath + "games.csv");
            out.getParentFile().mkdirs();
            out.createNewFile();
            writer = new PrintWriter(out);
            writer.write("Date, Season, Game Number, Game Code\n");
            for( String[] game : gamesList )
            {
                StringBuilder line = new StringBuilder();
                for( String string : game )
                {
                    line.append(string).append(",");
                }
                line.append("\n");
                writer.write(line.toString());
            }
            writer.close();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    private static void exportGoalsCSV()
    {
        PrintWriter writer;
        try {
            File out = new File(outputFilePath + "goals.csv");
            out.getParentFile().mkdirs();
            out.createNewFile();
            writer = new PrintWriter(out);
            writer.write("First Name,Last Name,Position,Team For,Team Against,Season,Game Number,Period,Game State,Primary Assister,Secondary Assister,Empty Net,Game Winning,Insurance\n");
            for (Player player : playersMap.toList()) {
                for (Goal goal : player.getGoalsList()) {
                    String line = player.getFirstName() +
                            "," +
                            player.getLastName() +
                            "," +
                            player.getPosition() +
                            "," +
                            goal.getTeamFor() +
                            "," +
                            goal.getTeamAgainst() +
                            "," +
                            goal.getSeasonName() +
                            "," +
                            goal.getGameNumber() +
                            "," +
                            goal.getPeriod() +
                            "," +
                            goal.getGameState() +
                            "," +
                            goal.getPrimaryAssiter() +
                            "," +
                            goal.getSecondaryAssister() +
                            "," +
                            goal.getEmptyNet() +
                            "," +
                            goal.getGameWinning() +
                            "," +
                            goal.getInsurance() +
                            "\n";
                    writer.write(line);
                }
            }
            writer.close();
        }
        catch ( IOException e) { e.printStackTrace(); }
    }

    private static void exportAssistsCSV()
    {
        PrintWriter writer;
        try {
            File out = new File(outputFilePath + "assists.csv");
            out.getParentFile().mkdirs();
            out.createNewFile();
            writer = new PrintWriter(out);
            writer.write("First Name,Last Name,Position,Team For,Team Against,Season,Game Number,Period,Game State,Scorer,Other Assister,Empty Net,Game Winning,Insurance\n");
            for (Player player : playersMap.toList()) {
                for (Assist assist : player.getAssistsList()) {
                    String line = player.getFirstName() +
                            "," +
                            player.getLastName() +
                            "," +
                            player.getPosition() +
                            "," +
                            assist.getTeamFor() +
                            "," +
                            assist.getTeamAgainst() +
                            "," +
                            assist.getSeasonName() +
                            "," +
                            assist.getGameNumber() +
                            "," +
                            assist.getPeriod() +
                            "," +
                            assist.getGameState() +
                            "," +
                            assist.getScorer() +
                            "," +
                            assist.getOtherAssister() +
                            "," +
                            assist.getEmptyNet() +
                            "," +
                            assist.getGameWinning() +
                            "," +
                            assist.getInsurance() +
                            "\n";
                    writer.write(line);
                }
            }
            writer.close();
        }
        catch(FileNotFoundException e){e.printStackTrace();}
        catch (IOException e) { e.printStackTrace(); }
    }

    private static void exportPlayersCSV()
    {
        PrintWriter writer;
        try {
            File out = new File(outputFilePath + "players.csv");
            out.getParentFile().mkdirs();
            out.createNewFile();
            writer = new PrintWriter(out);
            writer.write("First Name,Last Name,Position,Birthday,Draft Status,Games List\n");
            for (Player player : playersMap.toList()) {
                String line = player.getFirstName() + "," +
                        player.getLastName() + "," +
                        player.getPosition() + "," +
                        player.getBirthdayDraftStatus().getBirthday() + "," +
                        player.getBirthdayDraftStatus().getDraftStatus() + ",";
                StringBuilder gamesList = new StringBuilder();
                for( int game: player.getGamesList() )
                {
                    gamesList.append(Integer.toString(game)).append(";");
                }
                line += gamesList.toString() + "\n";
                writer.write(line);
            }
            writer.close();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    private static void exportPenaltiesCSV()
    {
        PrintWriter writer;
        try {
            File out = new File(outputFilePath + "penalties.csv");
            out.getParentFile().mkdirs();
            out.createNewFile();
            writer = new PrintWriter(out);
            writer.write("First Name,Last Name,Position,TeamFor,TeamAgainst,Season,GameNumber,Period,Minutes,PenaltyName,PenaltyType,Offsetting\n");
            for (Player player : playersMap.toList()) {
                for (Penalty penalty : player.getPenaltiesList()) {
                    String line = player.getFirstName() +
                            "," +
                            player.getLastName() +
                            "," +
                            player.getPosition() +
                            "," +
                            penalty.getTeamFor() +
                            "," +
                            penalty.getTeamAgainst() +
                            "," +
                            penalty.getSeasonName() +
                            "," +
                            penalty.getGameNumber() +
                            "," +
                            penalty.getPeriod() +
                            "," +
                            penalty.getMinutes() +
                            "," +
                            penalty.getPenaltyName() +
                            "," +
                            penalty.getPenaltyType() +
                            "," +
                            penalty.getOffsetting() +
                            "\n";
                    writer.write(line);
                }
            }
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void scrape( String league, int firstGame, int lastGame )
    {
        gamesList = new ArrayList<>();
        outputFilePath = "out/csv/" + league + "/" + firstGame + "to" + lastGame + "/";
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        WebDriver gamePageDriver = new ChromeDriver(chromeOptions);
        playerPageDriver = new ChromeDriver(chromeOptions);
        String urlBase = getURLBase(league);
        playersMap = playersMap.newEmpty();

        gameLoop:
        for( int gameCode = firstGame; gameCode <= lastGame; gameCode++ ) {
            System.out.println("Game " + (gameCode - firstGame) + " out of " + (lastGame-firstGame) + " - " + ((long)(gameCode-firstGame)/(lastGame-firstGame)) + "% done");
            // Skip a bunch of unused game codes in the OHL
            if (league.equals("OHL") && (26597 < gameCode && gameCode > 999999))
                continue;

            String gameURL = urlBase + "/gamecentre/" + gameCode + "/boxscore";
            Elements awaySkaters;
            Elements homeSkaters;
            Document parsedHTML;
            String seasonName = "";
            String gameDateString = "";

            // Try five times to get player stats
            for (int openAttempts = 0; true; openAttempts++) {
                try {
                    Thread.sleep(openAttempts * 10);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gamePageDriver.get(gameURL);
                String innerHTML = gamePageDriver.getPageSource();
                parsedHTML = Jsoup.parse(innerHTML);
                try{seasonName = consolidateSeasonName(parsedHTML.select("[data-reactid=\".0.0.0.3\"]").get(0).ownText(), league);}
                catch (IndexOutOfBoundsException e){}
                awaySkaters = parsedHTML.select("tbody[data-reactid=\".0.0.3.0.2.0.1.2.0.1\"] .table__tr--dark");
                homeSkaters = parsedHTML.select("tbody[data-reactid=\".0.0.3.0.2.1.1.2.0.1\"] .table__tr--dark");
                if (awaySkaters.size() != 0 || homeSkaters.size() != 0 || seasonName.length() != 0)
                    break;
                if (openAttempts > 5)
                    continue gameLoop;
            }

            Pattern datePattern = Pattern.compile("([A-z]{3,} \\d{1,2}[a-z]{2} \\d{4})");
            Matcher dateMatcher;
            dateMatcher = datePattern.matcher(parsedHTML.select("[data-reactid=\".0.0.0.4\"]").get(0).ownText());
            while(dateMatcher.find()) gameDateString = dateMatcher.group();
            int gameNumber = Integer.parseInt(parsedHTML.select("[data-reactid=\".0.0.0.3\"]").get(0).ownText().split(" ")[2]);
            String homeTeamName = parsedHTML.select("[data-reactid=\".0.0.0.2\"]").get(0).attr("class").split(" ")[1].split("-")[4];
            String awayTeamName = parsedHTML.select("[data-reactid=\".0.0.0.0\"]").get(0).attr("class").split(" ")[1].split("-")[4];
            // Save players in the game to memory if they don't already exist
            for( Element playerElement : homeSkaters)
                playerBuilder(playerElement, league, gameCode);
            for( Element playerElement : awaySkaters)
                playerBuilder(playerElement, league, gameCode);

            // Saves all goals in the game to their respective players
            Element goalsTable = parsedHTML.select("[data-reactid=\".0.0.3.0.7\"]").first();
            for( Element goalElement : goalsTable.children().subList(1, goalsTable.children().size()) )
                goalBuilder(goalElement, seasonName, gameCode, homeTeamName, awayTeamName);

            // Saves all penalties in the game to their respective players
            Element penaltyTable = parsedHTML.selectFirst("[data-reactid=\".0.0.3.0.8\"]");
            for( Element penaltyElement : penaltyTable.children().subList(1, penaltyTable.children().size()))
                penaltyBuilder(penaltyElement, seasonName, gameCode, homeTeamName, awayTeamName);

            gamesList.add(new String[]{gameDateString, seasonName, Integer.toString(gameNumber), Integer.toString(gameCode)});
        }
        gamePageDriver.close();
        playerPageDriver.close();
    }

    private static void penaltyBuilder(Element penaltyElement, String seasonName, int gameNumber, String homeTeamName, String awayTeamName)
    {
        //period, subject, teamFor, teamAgainst, seasonName, gameNumber, minutes, offsetting, major, misconduct, gameInfraction
        int period = Character.getNumericValue(penaltyElement.select("div").get(3).ownText().charAt(0));
        if(penaltyElement.selectFirst("a").ownText().length() == 0)
            return;
        String penaltyTakerName = nameFixer(penaltyElement.selectFirst("a").ownText());
        Player penaltyTaker = playersMap.get(penaltyTakerName);
        String teamFor = penaltyElement.selectFirst("div").attr("class").split(" ")[1].split("-")[3];
        String teamAgainst = teamFor.equals(homeTeamName) ? awayTeamName : homeTeamName;
        int minutes = 0;
        Boolean offsetting = true;
        String penaltyType = "Unnasigned";
        Element infoBox = penaltyElement.select("div").get(4);
        String penaltyName = infoBox.selectFirst("span").select("span").get(2).ownText();
        Elements flags = penaltyElement.select("span");
        for( Element flag : flags )
        {
            if( flag.ownText().contains("Power Play"))
                offsetting = false;
            else if( flag.ownText().toLowerCase().contains("minor"))
                penaltyType = "Minor";
            else if(flag.ownText().toLowerCase().contains("major"))
                penaltyType = "Major";
            else if(flag.ownText().toLowerCase().contains("misconduct"))
                penaltyType = "Misconduct";
            else if(flag.ownText().contains(":"))
                minutes = Integer.parseInt(flag.ownText().split(":")[0]);
        }
        //Surround in a try-catch statement to work around assigning penalties to goalies (who aren't added to PlayersMap)
        try{penaltyTaker.addPenalty(new Penalty(period, penaltyTaker, teamFor, teamAgainst, seasonName, gameNumber, minutes, offsetting, penaltyType, penaltyName));}
        catch(NullPointerException ignored){}
    }

    private static void playerBuilder( Element playerElement, String league, int gameNumber )
    {
        Elements cells = playerElement.select("td");
        String position = cells.get(0).ownText();
        Element nameElement = cells.get(2).select("a").get(0);
        String nameRaw = nameElement.ownText();
        String firstName = nameRaw.split(",")[1].split(" - ")[0].split(" \\*")[0].trim();
        String lastName = nameRaw.split(",")[0];
        String name = lastName + ", " + firstName;
        Player player = playersMap.get(name);
        if( player == null )
        {
            BirthdayDraftStatusContainer birthdayDraftStatusContainer = getBirthdayAndDraftStatus(league, nameElement.attr("href"));
            player = new Player(firstName, lastName, position, birthdayDraftStatusContainer);
            playersMap.put(name, player);
        }
        player.getGamesList().add(gameNumber);
    }

    private static String nameFixer( String name )
    {
        if(name.equals("Jordan Ty Fournier"))
            return "Fournier, Jordan Ty";
        String[] nameParts = name.split(" ");
        String firstName = nameParts[0];
        StringBuilder lastName = new StringBuilder(nameParts[1]);
        for(String namePart : Arrays.copyOfRange(nameParts, 2, nameParts.length))
            lastName.append(" ").append(namePart);
        return lastName + ", " + firstName;
    }

    private static void goalBuilder( Element goalElement, String seasonName, int gameNumber, String homeTeamName, String awayTeamName)
    {
        // period, subject, teamFor, teamAgainst, season, gameNumber, primary assister, secondary assister
        int period;
        Player scorer;
        String teamFor;
        String teamAgainst;
        Player primAssister;
        Player secAssister = null;
        GameState gameState = GameState.EVEN_STRENGTH;
        Boolean emptyNet = false;
        Boolean insurance = false;
        Boolean gameWinning = false;
        Boolean penaltyShot = false;

        period = Arrays.asList(1, 2, 3).contains(Character.getNumericValue(goalElement.select("div").get(3).ownText().charAt(0))) ? Character.getNumericValue(goalElement.select("div").get(3).ownText().charAt(0)) : 4;
        String scorerName = nameFixer(goalElement.select("div").get(5).selectFirst("a").ownText());
        scorer = playersMap.get(scorerName);
        teamFor = goalElement.selectFirst("div").attr("class").split(" ")[1].split("-")[4];
        teamAgainst = teamFor.equals(homeTeamName) ? awayTeamName : homeTeamName;
        // Try to select the name of the primary and secondary assisters, exception thrown if they don't exist, so set player to null
        try{String primAssisterName = nameFixer(goalElement.select("span").get(3).selectFirst("a").ownText());
            primAssister = playersMap.get(primAssisterName);}
        catch(NullPointerException e){primAssister = null;}
        catch(IndexOutOfBoundsException e){primAssister = null; secAssister = null;}
        try{String secAssisterName = nameFixer(goalElement.select("span").get(6).selectFirst("a").ownText());
            secAssister = playersMap.get(secAssisterName);}
        catch(NullPointerException e){secAssister = null;}
        catch(IndexOutOfBoundsException e){penaltyShot = true;}
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
        try{scorer.addGoal(new Goal(period, scorer, teamFor, teamAgainst, seasonName, gameNumber, primAssister, secAssister, emptyNet, gameWinning, insurance, gameState, penaltyShot));}
        catch (NullPointerException e){System.out.println("Error: Player " + scorerName + " not found in PlayersMap. May be goalie or other issue.");return;}
        if(primAssister != null)
            primAssister.addAssist(new Assist(period, primAssister, teamFor, teamAgainst, seasonName, gameNumber, scorer, secAssister, emptyNet, gameWinning, insurance, true, gameState));
        if(secAssister != null)
            secAssister.addAssist(new Assist(period, secAssister, teamFor, teamAgainst, seasonName, gameNumber, scorer, primAssister, emptyNet, gameWinning, insurance, false, gameState));
    }

    private static String getURLBase( String league )
    {
        switch( league )
        {
            case "OHL": return "http://www.ontariohockeyleague.com";
            case "WHL": return "http://www.whl.ca";
            case "QMJHL": return "http://theqmjhl.ca";
            //default: throw new Exception("Invalid league name: " + league + " is not one of OHL, WHL, QMJHL");
        }
        return ""; //Will throw exception in Selenium if nothing is returned, but this shouldn't happen
    }

    private static String consolidateSeasonName(String season, String league)
    {
        Pattern seasonPattern = Pattern.compile("(\\d{4}\\s?-\\s?\\d{2,4})");
        Matcher seasonMatcher = seasonPattern.matcher(season);
        String seasonName = "";
        while(seasonMatcher.find()){seasonName=seasonMatcher.group(0);}
        return seasonName;
    }

    private static BirthdayDraftStatusContainer getBirthdayAndDraftStatus( String league, String href)
    {
        String url = getURLBase(league) + href;
        playerPageDriver.get(url);
        String innerHTML = playerPageDriver.getPageSource();
        Elements parsedHTML = Jsoup.parse(innerHTML).select("body").first().children();
        // Check to wait for the page to fully load
        while(((( league.equals("WHL") || league.equals("OHL") ) && parsedHTML.select("[data-reactid=\".0.0.0.0.2.3.1\"]").first() == null) ) || (league.equals("QMJHL") && parsedHTML.select(".info-con-table01").first().selectFirst("td") == null))
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            playerPageDriver.get(playerPageDriver.getCurrentUrl());
            innerHTML = playerPageDriver.getPageSource();
            parsedHTML = Jsoup.parse(innerHTML).select("body").first().children();
        }

        LocalDate birthday;
        DraftStatusContainer draftStatus;

        if (league.equals("QMJHL"))
        {
            // Get the player's birthday
            try {
                Element qmjhlPlayerPanel = parsedHTML.select(".info-con-table01").first();
                Element row = qmjhlPlayerPanel.select("tr").get(2);
                String birthdayText = row.selectFirst("span").ownText();
                if(birthdayText.equals("0000-00-00"))
                    birthday = LocalDate.MIN;
                else{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    birthday = LocalDate.parse(birthdayText, formatter);
                }
            }
            catch( Exception e )
            {
                birthday = LocalDate.MIN;
            }
            // Get the player's draft status
            try{
                Element qmjhlDraftPanel = parsedHTML.select(".info-con-table02").first();
                Element row = qmjhlDraftPanel.selectFirst("td");
                String statusText = row.ownText();
                if( !statusText.contains("NHL"))
                    draftStatus = new DraftStatusContainer("Undrafted", league);
                else
                    draftStatus = new DraftStatusContainer(statusText, league);
            }
            catch ( Exception e )
            {
                draftStatus = new DraftStatusContainer("Undrafted", league);
            }
            return new BirthdayDraftStatusContainer(birthday, draftStatus);
        }
        else{
            String birthdayText = parsedHTML.select("[data-reactid=\".0.0.0.0.2.3.1\"]").first().ownText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try{birthday = LocalDate.parse(birthdayText, formatter);}
            catch (DateTimeException e){birthday = LocalDate.MIN;}
            try{
                Element draftTable = parsedHTML.select("[data-reactid=\".0.0.0.0.2.6.1\"]").first();
                String statusText = draftTable.select("div").get(1).ownText();
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
