package edu.umich.lib.libraryIdentifier.callnumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LCCallNumberSimple extends AbstractCallNumber {

    public String letters = "";
    public String digits = "";
    public String decimals = "";
    public String rest = "";


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // THe letters for a callnumber are either one letter, two letters, a K followed by 0-2 letters, or
    // the three letter sequence LAW

    public static String letter_pat = "(?<letters>(?:LAW|law|Law|[Kk]\\p{L}{0,2}|\\p{L}{1,2}))";

    public static Pattern lc_start = Pattern.compile(
//      "^\\s*(?<letters>[KkLl]?\\p{L}{1,2})[-\\s]*" + // 1-2 (3 in the Ks) initial letters, plus optional whitespace
            "^\\s*" + letter_pat + "\\s*" +
                    "(?<digits>\\d{1,5}(?!\\d))" +                  // 1-5 digits
                    "(?:\\.(?<decimals>\\d+))?" +   // an optional decimal ('.' plus digits)
                    "(?<rest>.*)$"        // Whatever's left
    );


    // When searching, we'll often want a range query that starts with only letters
    // That can be a single letter, any two-letter combination, or a set of three
    // letters starting with "K" (books about legal issues) or "L" (more of the same).
    // @TODO Put a guard around letter-only queries so we only accept them when an argument to the constructor says to.
//  public static Pattern acceptable_only_letters = Pattern.compile("^\\p{L}{1,3}$");

    public static Pattern acceptable_only_letters = Pattern.compile(letter_pat + "\\s*$");

    public LCCallNumberSimple(String str) {
        original = str;
        trimmedOriginal = trimPunctuation(str.trim()).trim().toLowerCase();
        Matcher m = lc_start.matcher(trimmedOriginal);
        if (m.matches()) {
            isValid = true;
            letters = m.group("letters");
            digits = m.group("digits");
            decimals = m.group("decimals");
            rest = m.group("rest");
        } else {
            LOGGER.debug("LC Callnumber '" + trimmedOriginal + "' is invalid.");
            isValid = false;
        }
        if (hasAcceptableTruncatedKey()) {
            LOGGER.debug("Original '" + trimmedOriginal + "'matches one-acceptable_only pattern");
        }
    }

    public Logger logger() {
        return LOGGER;
    }

    @Override
    public Boolean hasValidKey() {
        return isValid;
    }

    @Override
    public String validKey() {
        return collationKey();
    }

    @org.jetbrains.annotations.Nullable
    public String collationKey() {
        if (isValid) {
            String key = collationLetters() + collationDigits() + collationDecimals() + collationRest();
            return collapseSpaces(key);
        } else {
            return null;
        }
    }

    @Override
    public Boolean hasAcceptableTruncatedKey() {
        return isAcceptableTruncatedCallnumber(original);
    }

    @Override
    public String acceptableTruncatedKey() {
        if (hasAcceptableTruncatedKey()) {
            return trimmedOriginal;
        } else {
            return null;
        }
    }

    @Override
    public String invalidKey() {
        return cleanupFreetext(trimmedOriginal);
    }


    private String collationLetters() {
        return letters;
    }

    private String collationDigits() {
        Integer digit_length = digits.length();
        return digit_length + digits;
    }

    private String collationDecimals() {
        if (decimals == null) {
            return "";
        } else {
            return "." + decimals;
        }
    }

    private String collationRest() {
        if ((rest == null) || (rest.equals(""))) {
            return "";
        } else {
            return " " + cleanupFreetext(rest);
        }
    }

    private String cleanupFreetext(String str) {
        if (str == null) return "";
        String s = str.trim();
        if (s.equals("")) {
            return s;
        }
        String rv = replaceDotBeforeLetterWithSpace(s);
        rv = removeDotsBetweenLetters(rv);
        rv = removeNonDecimalPointPunctuation(rv);
        rv = forceSpaceBetweenDigitAndLetter(rv);
        return collapseSpaces(rv);
    }


    private Boolean isAcceptableTruncatedCallnumber(String str) {
        return acceptable_only_letters.matcher(str.trim().toLowerCase()).matches();
    }


    private String removeDotsBetweenLetters(String str) {
        return str.replaceAll("(\\p{L})\\.(\\p{L})", "$1$2");
    }

    private String replaceDotBeforeLetterWithSpace(String str) {
        return str.replaceAll("\\s+\\.(\\p{L})", " $1");
    }

    private String removeNonDecimalPointPunctuation(String str) {
        return str.replaceAll("(\\d)\\.(\\d)", "$1AAAAA$2")
                .replaceAll("\\p{P}", "")
                .replaceAll("(\\d)AAAAA(\\d)", "$1.$2");
    }

    private String forceSpaceBetweenDigitAndLetter(String str) {
        return str.replaceAll("(\\d)(\\p{L})", "$1 $2");
    }

}


