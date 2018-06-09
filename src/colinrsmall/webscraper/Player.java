package colinrsmall.webscraper;

import java.util.ArrayList;

class Player {

    private String firstName, lastName, position;
    private BirthdayDraftStatusContainer birthdayDraftStatus;
    private ArrayList<Goal> goalsList;
    private ArrayList<Penalty> penaltiesList;
    private ArrayList<Assist> assistsList;
    private ArrayList<Integer> gamesList;

    Player(String firstName, String lastName, String position, BirthdayDraftStatusContainer c){
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.birthdayDraftStatus = c;
        goalsList = new ArrayList<>();
        penaltiesList = new ArrayList<>();
        assistsList = new ArrayList<>();
        gamesList = new ArrayList<>();
    }

    @Override
    public String toString()
    {
        return this.firstName + " " + this.lastName + " - " + this.position;
    }

    public BirthdayDraftStatusContainer getBirthdayDraftStatus()
    {
        return birthdayDraftStatus;
    }

    public void addGoal(Goal g)
    {
        goalsList.add(g);
    }

    public void addAssist(Assist a)
    {
        assistsList.add(a);
    }

    public void addPenalty(Penalty p)
    {
        penaltiesList.add(p);
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getPosition()
    {
        return position;
    }

    public ArrayList<Goal> getGoalsList()
    {
        return goalsList;
    }

    public ArrayList<Penalty> getPenaltiesList()
    {
        return penaltiesList;
    }

    public ArrayList<Assist> getAssistsList()
    {
        return assistsList;
    }

    public ArrayList<Integer> getGamesList()
    {
        return gamesList;
    }
}

// each play object keeps track of # of plays by team in that game

