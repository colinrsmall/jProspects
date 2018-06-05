package colinrsmall.webscraper;

import java.util.Date;
import java.util.HashMap;

public class Game
{
    Date gameDate;
    String league;
    int gameNumber;
    HashMap<String, Player> homeTeamPlayers;
    HashMap<String, Player> awayTeamPlayers;

    public Game( int gameNumber ){
        this.gameNumber = gameNumber;
    }
}