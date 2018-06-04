package colinrsmall.webscraper;

public class Penalty extends Play
{
    int minutes;
    Boolean offsetting;
    Boolean major;
    Boolean misconduct; //Player was given a game misconduct?
    Boolean gameInfraction; //Any penalty except for roughing, fighting, etc.

    public Penalty(int season, int period, int gameState, Boolean teamBehind, Player subject, Team teamFor, Team teamAgainst, int minutes, Boolean offsetting, Boolean major, Boolean misconduct, Boolean gameInfraction)
    {
        super(season, period, gameState, teamBehind, subject, teamFor, teamAgainst);
        this.minutes = minutes;
        this.offsetting = offsetting;
        this.major = major;
        this.misconduct = misconduct;
        this.gameInfraction = gameInfraction;
    }
}
