package colinrsmall.webscraper;

import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;

public class WebScraper {

    private final int WHL_2017_START = 1014621;
    private final int WHL_2017_END = 1015412;

    private static MutableMap<String, Season> seasonsMap = Maps.mutable.empty();

    public static void main(String[] args)
    {
        System.setProperty("webdriver.chrome.driver", "/Users/colinrsmall/Documents/GitHub/jProspects/src/colinrsmall/chromedriver/chromedriver");
        scrape("OHL", 22381, 22382);
    }

    private static void scrape( String league, int firstGame, int lastGame )
    {
        String urlBase = getURLBase(league);
        WebDriver driver = new ChromeDriver();
        seasonsMap = seasonsMap.newEmpty();

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

            // Try five times to get player stats
            for (int openAttempts = 0; true; openAttempts++) {
                driver.get(gameURL);
                String innerHTML = driver.getPageSource();
                Document parsedHTML = Jsoup.parse(innerHTML);
                seasonName = consolidateSeasonName(parsedHTML.select("[data-reactid=\".0.0.0.3\"]").get(0).ownText(), league);
                homeTeamName = parsedHTML.select("[data-reactid=\".0.0.0.2.2\"]").get(0).ownText();
                awayTeamName = parsedHTML.select("[data-reactid=\".0.0.0.0.2\"]").get(0).ownText();
                gameNumber = Integer.parseInt(parsedHTML.select("[data-reactid=\".0.0.0.0.2\"]").get(0).ownText().split(" ")[2]);
                awaySkaters = parsedHTML.select("tbody[data-reactid=\".0.0.3.0.2.0.1.2.0.1\"] .table__tr--dark");
                homeSkaters = parsedHTML.select("tbody[data-reactid=\".0.0.3.0.2.1.1.2.0.1\"] .table__tr--dark");
                if (awaySkaters.size() != 0 || homeSkaters.size() != 0)
                    break;
                if (openAttempts > 5)
                    continue gameLoop;
            }

            // Initialize new Game and new Team and new Season if needed
            Season season = seasonsMap.getIfAbsentPut(seasonName, new Season(seasonName));
            Team homeTeam = season.getTeamsMap().getIfAbsentPut(homeTeamName, new Team(homeTeamName));
            Team awayTeam = season.getTeamsMap().getIfAbsentPut(awayTeamName, new Team(awayTeamName));
            Game game = season.getGamesMap().getIfAbsentPut(gameNumber, new Game(gameNumber));
        }

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
}
