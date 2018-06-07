package colinrsmall.webscraper;

class Goal extends Play
{
    private Player primaryAssiter;
    private Player secondaryAssister;
    private Boolean emptyNet;
    private Boolean gameWinning;
    private Boolean insurance;
    private GameState gameState;
    private Boolean penaltyShot;

    public Goal(int period, Player subject, String teamFor, String teamAgainst, String seasonName, int gameNumber, Player primaryAssiter, Player secondaryAssister, Boolean emptyNet, Boolean gameWinning, Boolean insurance, GameState gameState, Boolean penaltyShot)
    {
        super(period, subject, teamFor, teamAgainst, seasonName, gameNumber);
        this.primaryAssiter = primaryAssiter;
        this.secondaryAssister = secondaryAssister;
        this.emptyNet = emptyNet;
        this.gameWinning = gameWinning;
        this.insurance = insurance;
        this.gameState = gameState;
        this.penaltyShot = penaltyShot;
    }

    public Player getPrimaryAssiter()
    {
        return primaryAssiter;
    }

    public Player getSecondaryAssister()
    {
        return secondaryAssister;
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

    public GameState getGameState()
    {
        return gameState;
    }
}

