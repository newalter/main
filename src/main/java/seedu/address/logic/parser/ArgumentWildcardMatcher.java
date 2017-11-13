package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

//@@author newalter
/**
 * Converts a list of string of unprocessed keywords with wildcard symbol "*" and "?"
 * into a list of lowercase regular expression matching the keywords.
 */
public class ArgumentWildcardMatcher {

    /**
     * Converts a list of {@code keywords} with wildcard symbol "*" and "?"
     * into a list of lowercase regular expression matching the keywords.
     */
    public static List<String> processKeywords(List<String> keywords) {
        requireNonNull(keywords);
        ArrayList<String> processedKeywords = new ArrayList<>();
        for (String keyword : keywords) {
            processedKeywords.add(keyword.toLowerCase().replace("*", "\\S*").replace("?", "\\S"));
        }
        return processedKeywords;
    }
}
