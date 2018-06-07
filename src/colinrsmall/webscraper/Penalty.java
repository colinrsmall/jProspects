package colinrsmall.webscraper;

class Penalty extends Play
{
    private int minutes;
    private Boolean offsetting;
    private String penaltyType;
    private String penaltyName;

    Penalty(int period, Player subject, String teamFor, String teamAgainst, String seasonName, int gameNumber, int minutes, Boolean offsetting, String penaltyType, String penaltyName)
    {
        super(period, subject, teamFor, teamAgainst, seasonName, gameNumber);
        this.minutes = minutes;
        this.offsetting = offsetting;
        this.penaltyType = penaltyType;
        this.penaltyName = penaltyName;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public Boolean getOffsetting()
    {
        return offsetting;
    }

    public String getPenaltyType()
    {
        return penaltyType;
    }

    public String getPenaltyName()
    {
        return penaltyName;
    }
}
