package colinrsmall.webscraper;

import java.util.ArrayList;

public class Player {

    private String firstName, lastName, position;
    private BirthdayDraftStatusContainer birthdayDraftStatus;
    private ArrayList<Goal> goalsList;
    private ArrayList<Penalty> penaltiesList;
    private ArrayList<Assist> assistsList;

    public Player(String firstName, String lastName, String position, BirthdayDraftStatusContainer c){
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.birthdayDraftStatus = c;
        goalsList = new ArrayList<>();
        penaltiesList = new ArrayList<>();
        assistsList = new ArrayList<>();
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
}

// each play object keeps track of # of plays by team in that game

