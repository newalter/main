package guitests.guihandles;

import java.util.Optional;

import javafx.scene.control.ListView;
import seedu.address.model.meeting.Meeting;
import seedu.address.ui.MeetingCard;

//@@author LimYangSheng
/**
 * Provides a handle for {@code MeetingListPanel} containing the list of {@code MeetingCard}.
 */
public class MeetingListPanelHandle extends NodeHandle<ListView<MeetingCard>> {
    public static final String MEETING_LIST_VIEW_ID = "#meetingListView";

    public MeetingListPanelHandle(ListView<MeetingCard> meetingListPanelNode) {
        super(meetingListPanelNode);
    }

    /**
     * Returns the meeting card handle of a meeting associated with the {@code index} in the list.
     */
    public MeetingCardHandle getMeetingCardHandle(int index) {
        return getMeetingCardHandle(getRootNode().getItems().get(index).meeting);
    }

    /**
     * Returns the {@code MeetingCardHandle} of the specified {@code meeting} in the list.
     */
    public MeetingCardHandle getMeetingCardHandle(Meeting meeting) {
        Optional<MeetingCardHandle> handle = getRootNode().getItems().stream()
                .filter(card -> card.meeting.equals(meeting))
                .map(card -> new MeetingCardHandle(card.getRoot()))
                .findFirst();
        return handle.orElseThrow(() -> new IllegalArgumentException("Meeting does not exist."));
    }

}
