package colinrsmall.webscraper;

public class DraftStatusContainer
{
    private Boolean drafted;
    private int draftYear;
    private int draftRound;
    private int draftPick;
    private String draftTeam;

    DraftStatusContainer(String draftStatus, String league)
    {
        if(draftStatus.equals("Undrafted"))
        {
            drafted = false;
        }
        else{
            String[] textComponents = draftStatus.split(" ");
            drafted = true;
            switch (league)
            {
                case "QMJHL": draftYear = Integer.parseInt(textComponents[3]);
                              draftRound = Integer.parseInt(textComponents[5]);
                              draftPick = Integer.parseInt(textComponents[6].replace("(", "").replace(")", "").replace("#", ""));
                              draftTeam = textComponents[7];
                              return;
                case "WHL": // Do the below ops, WHL and OHL have the same format
                case "OHL": draftYear = Integer.parseInt(textComponents[3].replace("(", "").replace(")", ""));
                            draftRound = Integer.parseInt(textComponents[5]);
                            draftPick = Integer.parseInt(textComponents[6].replace("(", "").replace(")", "").replace("#", ""));
                            draftTeam = textComponents[2];
                            return;
                default: drafted = false;
            }
        }
    }

    @Override
    public String toString()
    {
        return draftTeam + ": " + draftYear + ", R: " + draftRound + ", OA: " + draftPick;
    }

    public Boolean getDrafted()
    {
        return drafted;
    }

    public int getDraftYear()
    {
        return draftYear;
    }

    public int getDraftRound()
    {
        return draftRound;
    }

    public int getDraftPick()
    {
        return draftPick;
    }

    public String getDraftTeam()
    {
        return draftTeam;
    }
}
