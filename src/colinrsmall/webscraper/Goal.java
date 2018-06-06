package colinrsmall.webscraper;

public class Goal extends Play
{
    Player primaryAssiter;
    Player secondaryAssister;
    Boolean emptyNet;
    Boolean gameWinning;
    Boolean insurance;
    GameState gameState;

    public Goal(int period, Player subject, String teamFor, String teamAgainst, String seasonName, int gameNumber, Player primaryAssiter, Player secondaryAssister, Boolean emptyNet, Boolean gameWinning, Boolean insurance, GameState gameState)
    {
        super(period, subject, teamFor, teamAgainst, seasonName, gameNumber);
        this.primaryAssiter = primaryAssiter;
        this.secondaryAssister = secondaryAssister;
        this.emptyNet = emptyNet;
        this.gameWinning = gameWinning;
        this.insurance = insurance;
        this.gameState = gameState;
    }
}

