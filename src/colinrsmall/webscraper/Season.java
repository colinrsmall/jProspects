package colinrsmall.webscraper;

import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Maps;

import java.util.ArrayList;

class Season
{
    MutableMap<String, Team> teamsMap;
    MutableMap<Integer, Game> gamesMap;
    private String seasonName;
    private int firstGame;
    private int lastGame;

    public String getSeasonName(){
        return seasonName;
    }

    Season(String seasonName)
    {
        this.seasonName = seasonName;
        teamsMap = Maps.mutable.empty();
    }

    public MutableMap<String, Team> getTeamsMap()
    {
        return teamsMap;
    }

    public MutableMap<Integer, Game> getGamesMap()
    {
        return gamesMap;
    }
}
