package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.ResizeCommand;


public class ResizeCommandParserTest {

    private ResizeCommandParser parser = new ResizeCommandParser();

    @Test
    public void parse_validArgs_returnsResizeCommand() {
        assertParseSuccess(parser, "1280 720", new ResizeCommand(1280, 720));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // invalid argument
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ResizeCommand.MESSAGE_USAGE));

        // invalid number of arguments
        assertParseFailure(parser, "800 600 600",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ResizeCommand.MESSAGE_USAGE));

        // invalid arguments that are out of bound
        assertParseFailure(parser, "1280 721",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ResizeCommand.MESSAGE_USAGE));


    }
}
