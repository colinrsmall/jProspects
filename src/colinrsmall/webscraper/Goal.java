package colinrsmall.webscraper;

public class Goal extends Play
{
    Player primaryAssiter;
    Player secondaryAssister;

    public Goal(int season, int period, int gameState, Boolean teamBehind, Player subject, Team teamFor, Team teamAgainst, Player primaryAssiter, Player secondaryAssister)
    {
        super(season, period, gameState, teamBehind, subject, teamFor, teamAgainst);
        this.primaryAssiter = primaryAssiter;
        this.secondaryAssister = secondaryAssister;
    }
}

