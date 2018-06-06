package colinrsmall.webscraper;

class Play {

    private int period;
    private Player subject;
    private String teamFor; //Team the player was playing on
    private String teamAgainst; //Team the player was playing against
    private String seasonName;
    private int gameNumber;

    public Play(int period, Player subject, String teamFor, String teamAgainst, String seasonName, int gameNumber)
    {
        this.period = period;
        this.subject = subject;
        this.teamFor = teamFor;
        this.teamAgainst = teamAgainst;
        this.seasonName = seasonName;
        this.gameNumber = gameNumber;
    }
}
