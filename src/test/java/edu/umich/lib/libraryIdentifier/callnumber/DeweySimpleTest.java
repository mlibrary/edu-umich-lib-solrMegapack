package edu.umich.lib.libraryIdentifier.callnumber;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class DeweySimpleTest {

    @ParameterizedTest
    @CsvFileSource(files = "src/test/java/edu/umich/lib/libraryIdentifier/callnumber/dewey_verification.tsv", delimiterString = "->")
    void collation_key(String original, String collation) {
        DeweySimple dewey = new DeweySimple(original);
        String key = dewey.collationKey();
        if (key == null) key = "null";
        assertEquals(collation.toString(), key);
    }
}