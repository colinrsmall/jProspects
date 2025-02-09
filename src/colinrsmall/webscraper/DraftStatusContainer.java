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
            try {
                switch (league) {
                    case "QMJHL":
                        draftYear = Integer.parseInt(textComponents[2].split(",")[0]);
                        draftRound = Integer.parseInt(textComponents[4]);
                        draftPick = Integer.parseInt(textComponents[5].replace("(", "").replace(")", "").replace("#", ""));
                        draftTeam = textComponents[6];
                        return;
                    case "WHL": // Do the below ops, WHL and OHL have the same format
                    case "OHL":
                        draftYear = Integer.parseInt(draftStatus.split("\\(")[1].split("\\)")[0]);
                        draftRound = Integer.parseInt(draftStatus.split(":")[1].split("\\(")[0].trim());
                        draftPick = Integer.parseInt(draftStatus.split("\\#")[1].split("\\)")[0]);
                        draftTeam = draftStatus.split(" - ")[1].split("\\(")[0].trim();
                        return;
                    default:
                        drafted = false;
                }
            }
            catch (IndexOutOfBoundsException e){drafted = false;}
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
