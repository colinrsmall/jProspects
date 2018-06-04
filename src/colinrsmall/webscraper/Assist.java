package colinrsmall.webscraper;

public class Assist extends Play
{
    Player scorer;
    Player otherAssister;

    public Assist(int season, int period, int gameState, Boolean teamBehind, Player subject, Team teamFor, Team teamAgainst, Player scorer, Player otherAssister)
    {
        super(season, period, gameState, teamBehind, subject, teamFor, teamAgainst);
        this.scorer = scorer;
        this.otherAssister = otherAssister;
    }
}
