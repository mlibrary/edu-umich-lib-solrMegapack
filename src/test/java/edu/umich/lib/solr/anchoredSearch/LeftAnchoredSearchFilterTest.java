package edu.umich.lib.solr.anchoredSearch;

import edu.umich.lib.solr.pluginScaffold.testSupport.ManualTokenStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


public class LeftAnchoredSearchFilterTest {


  @Test
  public void testOneToken() throws IOException {
    ManualTokenStream ts = new ManualTokenStream("Bill");

    LeftAnchoredSearchFilter lasf = new LeftAnchoredSearchFilter(ts);
    assertEquals(Collections.singletonList("Bill1"), lasf.nestedTerms().get(0));
  }

  @Test
  public void testNested() throws IOException {
    ManualTokenStream ts = new ManualTokenStream("Bill", Arrays.asList("John", "James"), "Dueber");

    String expected = "[Bill1, [John2, James2], Dueber3]";
    LeftAnchoredSearchFilter lasf = new LeftAnchoredSearchFilter(ts);
    String terms = lasf.nestedTermsAsString();
    assertEquals(expected, terms);
  }

}
