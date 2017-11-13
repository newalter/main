package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showFirstPersonOnly;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;


/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code AddMeetingCommand}.
 */
public class AddMeetingCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private String meetingName = "meeting1";
    private String meetingTime = "2017-11-20 15:00";

    @Test
    public void execute_unfilteredList_success() throws Exception {
        ReadOnlyPerson readOnlyPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = editPersonToHaveMeeting(readOnlyPerson);
        AddMeetingCommand addMeetingCommand = prepareCommand(INDEX_FIRST_PERSON, meetingName, meetingTime);
        String expectedMessage = String.format(AddMeetingCommand.MESSAGE_ADD_MEETING_SUCCESS, meetingName);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(addMeetingCommand, model, expectedMessage, expectedModel);

        // Tests for duplicate meeting
        assertCommandFailure(addMeetingCommand, model, AddMeetingCommand.MESSAGE_DUPLICATE_MEETING);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddMeetingCommand addMeetingCommand = prepareCommand(outOfBoundIndex, meetingName, meetingTime);
        assertCommandFailure(addMeetingCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidMeetingTime_throwsCommandException() throws Exception {
        String invalidMeetingTime = "2017-11 15:00";
        try {
            AddMeetingCommand addMeetingCommand = prepareCommand(INDEX_FIRST_PERSON, meetingName, invalidMeetingTime);
        } catch (IllegalValueException ive) {
            assertEquals(null, ive.getMessage(), Meeting.MESSAGE_TIME_CONSTRAINTS);
        }
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showFirstPersonOnly(model);

        ReadOnlyPerson personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = editPersonToHaveMeeting(personInFilteredList);
        AddMeetingCommand addMeetingCommand = prepareCommand(INDEX_FIRST_PERSON, meetingName, meetingTime);

        String expectedMessage = String.format(AddMeetingCommand.MESSAGE_ADD_MEETING_SUCCESS, meetingName);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);
        showFirstPersonOnly(expectedModel);

        assertCommandSuccess(addMeetingCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() throws Exception {
        showFirstPersonOnly(model);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        AddMeetingCommand addMeetingCommand = prepareCommand(outOfBoundIndex, meetingName, meetingTime);
        assertCommandFailure(addMeetingCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        String secondMeetingName = "meeting2";
        String secondMeetingTime = "2017-11-20 5:00";

        AddMeetingCommand firstMeetingCommand =
                new AddMeetingCommand(INDEX_FIRST_PERSON, meetingName, meetingTime);
        AddMeetingCommand secondMeetingCommand =
                new AddMeetingCommand(INDEX_FIRST_PERSON, meetingName, secondMeetingTime);
        AddMeetingCommand thirdMeetingCommand =
                new AddMeetingCommand(INDEX_FIRST_PERSON, secondMeetingName, meetingTime);

        // same values -> returns true
        AddMeetingCommand commandWithSameValues = new AddMeetingCommand(INDEX_FIRST_PERSON, meetingName, meetingTime);
        assertTrue(firstMeetingCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(firstMeetingCommand.equals(firstMeetingCommand));

        // null -> returns false
        assertFalse(firstMeetingCommand.equals(null));

        // different types -> returns false
        assertFalse(firstMeetingCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(firstMeetingCommand.equals(new AddMeetingCommand(INDEX_SECOND_PERSON, meetingName, meetingTime)));

        // different descriptor -> returns false
        assertFalse(firstMeetingCommand.equals(secondMeetingCommand));
        assertFalse(firstMeetingCommand.equals(thirdMeetingCommand));
    }


    /**
     * Returns a {@code AddMeetingCommand} with the parameter {@code meetingName} and {@code meetingTime}.
     */
    private AddMeetingCommand prepareCommand(Index index, String meetingName, String meetingTime) throws Exception {
        AddMeetingCommand addMeetingCommand = new AddMeetingCommand(index, meetingName, meetingTime);
        addMeetingCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return addMeetingCommand;
    }

    /**
     * adds the default meeting to the {@code readOnlyPerson}
     * @return an edited person
     * @throws IllegalValueException
     */
    private Person editPersonToHaveMeeting(ReadOnlyPerson readOnlyPerson) throws IllegalValueException {
        Set<Meeting> meetings = new HashSet<Meeting>(readOnlyPerson.getMeetings());
        Meeting newMeeting = new Meeting(readOnlyPerson, meetingName, meetingTime);
        meetings.add(newMeeting);
        Person editedPerson = new Person(readOnlyPerson);
        editedPerson.setMeetings(meetings);
        return editedPerson;
    }
}
