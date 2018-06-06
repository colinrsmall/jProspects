package colinrsmall.webscraper;

import java.time.LocalDate;

public class BirthdayDraftStatusContainer
{

    private LocalDate birthday;
    private DraftStatusContainer draftStatus;

    BirthdayDraftStatusContainer()
    {

    }

    public BirthdayDraftStatusContainer(LocalDate birthday, DraftStatusContainer draftStatus)
    {
        this.birthday = birthday;
        this.draftStatus = draftStatus;
    }

    public LocalDate getBirthday()
    {
        return birthday;
    }

    public DraftStatusContainer getDraftStatus()
    {
        return draftStatus;
    }

    public void setBirthday(LocalDate birthday)
    {
        this.birthday = birthday;
    }

    public void setDraftStatus(DraftStatusContainer draftStatus)
    {
        this.draftStatus = draftStatus;
    }
}
