package colinrsmall.webscraper;

class Play {

    int season;
    int period;
    int gameState;
    Boolean teamBehind;
    Player subject;
    Team teamFor; //Team the player was playing on
    Team teamAgainst; //Team the player was playing against

    Play(int season, int period, int gameState, Boolean teamBehind, Player subject, Team teamFor, Team teamAgainst)
    {
        this.season = season;
        this.period = period;
        this.gameState = gameState;
        this.teamBehind = teamBehind;
        this.subject = subject;
        this.teamFor = teamFor;
        this.teamAgainst = teamAgainst;
    }
}
