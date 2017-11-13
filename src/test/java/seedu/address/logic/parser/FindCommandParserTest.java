package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.FindCommand.FALSE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.predicate.AddressContainsKeywordsPredicate;
import seedu.address.model.person.predicate.ContainsTagsPredicate;
import seedu.address.model.person.predicate.EmailContainsKeywordsPredicate;
import seedu.address.model.person.predicate.NameContainsKeywordsPredicate;
import seedu.address.model.person.predicate.PhoneContainsKeywordsPredicate;

//@@author newalter
public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();
    private Predicate<ReadOnlyPerson> component1;
    private Predicate<ReadOnlyPerson> component2;
    private Predicate<ReadOnlyPerson> component3;
    private Predicate<ReadOnlyPerson> component4;
    private Predicate<ReadOnlyPerson> component5;


    @Test
    public void parse_invalidArg_throwsParseException() {
        // empty arg
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // no valid field present
        assertParseFailure(parser, " m/meeting ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

    }


    @Before
    public  void set_up() {
        component1 = new NameContainsKeywordsPredicate(Arrays.asList("alice", "bob"));
        component2 = new PhoneContainsKeywordsPredicate(Collections.singletonList("88887777"));
        component3 = new EmailContainsKeywordsPredicate(Collections.singletonList("alice@example.com"));
        component4 = new AddressContainsKeywordsPredicate(Collections.singletonList("Clementi"));
        component5 = new ContainsTagsPredicate(Arrays.asList("family", "friends"));
    }


    @Test
    public void parse_allFieldsPresent_returnsFindCommand() {
        ArrayList<Predicate<ReadOnlyPerson>> predicates = new ArrayList<>();
        predicates.addAll(Arrays.asList(component1, component2, component3, component4, component5));
        FindCommand expectedFindCommand = new FindCommand(predicates);

        // no leading and trailing whitespaces
        assertParseSuccess(parser,
                " n/Alice Bob e/alice@example.com a/Clementi t/family friends p/88887777", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, "  n/ Alice Bob \t e/alice@example.com "
                + "a/Clementi t/ family \n friends \t p/88887777 \t ", expectedFindCommand);

        // different order of fields
        assertParseSuccess(parser,
                " e/alice@example.com n/Alice t/family n/Bob a/Clementi t/friends p/88887777", expectedFindCommand);
    }

    @Test
    public void parse_someFieldsPresent_returnsFindCommand() {
        // two fields are present
        ArrayList<Predicate<ReadOnlyPerson>> predicates = new ArrayList<>();
        predicates.addAll(Arrays.asList(component1, FALSE, FALSE, FALSE, component5));
        FindCommand expectedFindCommand = new FindCommand(predicates);
        assertParseSuccess(parser, " n/Alice Bob t/family friends", expectedFindCommand);
        // different order of fields
        assertParseSuccess(parser, " t/family friends n/Alice Bob", expectedFindCommand);
        // different order of fields
        assertParseSuccess(parser, " t/family n/Alice t/friends n/Bob", expectedFindCommand);

        // one field is present
        predicates.clear();
        predicates.addAll(Arrays.asList(FALSE, component2, FALSE, FALSE, FALSE));
        assertParseSuccess(parser, " p/88887777", expectedFindCommand);
        // leading white space between prefix and keyword
        assertParseSuccess(parser, " p/     88887777", expectedFindCommand);

    }

}
