package edu.umich.lib.solr.filterTest;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.eclipse.jetty.util.IO;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class ManualTokenStreamTest {

  @Test
  public void oneToken() throws IOException {
    ManualTokenStream ts = new ManualTokenStream("[[Bill]]");
    assertEquals(TokenStreamTestHelpers.getTermsAsString(ts), "Bill");
  }

  @Test
  public void twoTokens() throws IOException {
//    ManualTokenStream ts = new ManualTokenStream("[[Bill], [Dueber]]");
    ManualTokenStream ts = new ManualTokenStream("Bill", "Dueber");
    List<String> expected = Arrays.asList("Bill", "Dueber");
    assertEquals(expected, TokenStreamTestHelpers.getTerms(ts));
  }

  @Test
  public void nestedTokens() throws IOException {
    ManualTokenStream mts = new ManualTokenStream();
    mts.add("Bill");
    mts.add(Arrays.asList("John", "James"));
    mts.add("Dueber");
    assertEquals("[Bill, [John, James], Dueber]", TokenStreamTestHelpers.getNestedTermsAsString(mts));
  }


}
