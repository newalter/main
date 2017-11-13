package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysMeeting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.MeetingCardHandle;
import guitests.guihandles.MeetingListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;


//@@author LimYangSheng
public class MeetingListPanelTest extends GuiUnitTest {

    private List<ReadOnlyPerson> typicalPersons;
    private List<Meeting> meetingList;
    private ObservableList<Meeting> observableMeetingList;
    private MeetingListPanelHandle meetingListPanelHandle;
    private Set<Meeting> meetingSet;

    @Before
    public void setUp() {
        typicalPersons = getTypicalPersons();
        meetingList = new ArrayList<>();

        Person firstPerson = (Person) typicalPersons.get(1);
        addMeetingToPerson(firstPerson, "Dinner", "2017-12-29 18:00");
        Person secondPerson = (Person) typicalPersons.get(2);
        addMeetingToPerson(secondPerson, "Dinner", "2017-12-30 18:00");

        observableMeetingList = FXCollections.observableList(meetingList);
        MeetingListPanel meetingListPanel = new MeetingListPanel(observableMeetingList);
        uiPartRule.setUiPart(meetingListPanel);
        meetingListPanelHandle = new MeetingListPanelHandle(getChildNode(meetingListPanel.getRoot(),
                MeetingListPanelHandle.MEETING_LIST_VIEW_ID));
    }

    @Test
    public void display() {
        for (int i = 0; i < meetingList.size(); i++) {
            Meeting expectedMeeting = meetingList.get(i);
            MeetingCardHandle actualCard = meetingListPanelHandle.getMeetingCardHandle(i);
            assertCardDisplaysMeeting(expectedMeeting, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    /**
     * Replace meeting of {@code person} with given parameters
     */
    private void addMeetingToPerson(Person person, String meetingName, String meetingTime) {
        try {
            Meeting meeting = new Meeting(person, meetingName, meetingTime);
            meetingSet = new HashSet<>();
            meetingSet.add(meeting);
            person.setMeetings(meetingSet);
            meetingList.add(meeting);
        } catch (IllegalValueException e) {
            throw new AssertionError("Meeting should be created successfully");
        }
    }
}
