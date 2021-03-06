package seedu.address.logic.commands;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Id;
import seedu.address.model.person.LastUpdated;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

//@@author derrickchua
/**
 * Deletes a person identified using it's last displayed index from the address book.
 */
public class NoteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "note";
    public static final String COMMAND_ALIAS = "n";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Modifies a note for the person identified by "
            + "the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + "NOTE]\n"
            + "Example: " + COMMAND_WORD + " 1 " + " Has 3 children.";

    public static final String MESSAGE_NOTE_SUCCESS = "Note Added: %1$s";

    private final Index targetIndex;
    private final Note note;

    public NoteCommand(Index targetIndex, Note note) {

        this.targetIndex = targetIndex;
        this.note = note;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToNote = lastShownList.get(targetIndex.getZeroBased());
        Name updatedName = personToNote.getName();
        Phone updatedPhone = personToNote.getPhone();
        Email updatedEmail = personToNote.getEmail();
        Address updatedAddress = personToNote.getAddress();
        Id updatedId = personToNote.getId();
        LastUpdated lastUpdated = personToNote.getLastUpdated();
        Set<Tag> updatedTags = personToNote.getTags();
        Set<Meeting> updatedMeetings = personToNote.getMeetings();

        ReadOnlyPerson updatedPerson =
                new Person (updatedName, updatedPhone, updatedEmail, updatedAddress,
                        this.note, updatedId, lastUpdated, updatedTags, updatedMeetings);

        try {
            model.updatePerson(personToNote, updatedPerson);
        }  catch (DuplicatePersonException dpe) {
            throw new CommandException("Person already exists in the AddressBook.");
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target person cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_NOTE_SUCCESS, updatedPerson));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NoteCommand // instanceof handles nulls
                && this.targetIndex.equals(((NoteCommand) other).targetIndex)); // state check
    }
}
