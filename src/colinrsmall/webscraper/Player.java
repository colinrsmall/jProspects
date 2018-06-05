package colinrsmall.webscraper;

import java.util.ArrayList;

public class Player {

    String firstName, lastName, position;
    ArrayList<Goal> goalsList;
    ArrayList<Penalty> penaltiesList;
    ArrayList<Assist> assistsList;
    ArrayList<Team> teamsList;

    public Player(String firstName, String lastName, String position){
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        goalsList = new ArrayList<>();
        penaltiesList = new ArrayList<>();
        assistsList = new ArrayList<>();
    }

}

// each play object keeps track of # of plays by team in that game

