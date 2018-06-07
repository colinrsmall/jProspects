package colinrsmall.webscraper;

public class Assist extends Play
{
    Player scorer;
    Player otherAssister;
    Boolean emptyNet;
    Boolean gameWinning;
    Boolean insurance;
    Boolean primary;
    GameState gameState;

    public Assist(int period, Player subject, String teamFor, String teamAgainst, String seasonName, int gameNumber, Player scorer, Player otherAssister, Boolean emptyNet, Boolean gameWinning, Boolean insurance, Boolean primary, GameState gameState)
    {
        super(period, subject, teamFor, teamAgainst, seasonName, gameNumber);
        this.scorer = scorer;
        this.otherAssister = otherAssister;
        this.emptyNet = emptyNet;
        this.gameWinning = gameWinning;
        this.insurance = insurance;
        this.primary = primary;
        this.gameState = gameState;
    }

    public Player getScorer()
    {
        return scorer;
    }

    public Player getOtherAssister()
    {
        return otherAssister;
    }

    public Boolean getEmptyNet()
    {
        return emptyNet;
    }

    public Boolean getGameWinning()
    {
        return gameWinning;
    }

    public Boolean getInsurance()
    {
        return insurance;
    }

    public Boolean getPrimary()
    {
        return primary;
    }

    public GameState getGameState()
    {
        return gameState;
    }
}
