package colinrsmall.webscraper;

public class Penalty extends Play
{
    int minutes;
    Boolean offsetting;
    String penaltyType;
    String penaltyName;

    public Penalty(int period, Player subject, String teamFor, String teamAgainst, String seasonName, int gameNumber, int minutes, Boolean offsetting, String penaltyType, String penaltyName)
    {
        super(period, subject, teamFor, teamAgainst, seasonName, gameNumber);
        this.minutes = minutes;
        this.offsetting = offsetting;
        this.penaltyType = penaltyType;
        this.penaltyName = penaltyName;
    }
}
