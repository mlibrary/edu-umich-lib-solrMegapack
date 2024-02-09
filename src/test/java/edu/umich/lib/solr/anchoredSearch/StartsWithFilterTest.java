package edu.umich.lib.solr.anchoredSearch;

import edu.umich.lib.solr.filterTest.TokenStreamTestHelpers;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import edu.umich.lib.solr.filterTest.ManualTokenStream;


public class StartsWithFilterTest {


  @Test
  public void testOneToken() throws IOException {
    ManualTokenStream ts = new ManualTokenStream("[[Bill], [Dueber]]");
    StartsWithFilter lasf = new StartsWithFilter(ts);
    assertEquals("Bill1 Dueber2", TokenStreamTestHelpers.getTermsAsString(lasf));
  }

  @Test
  public void testNested() throws IOException {
    ManualTokenStream ts = new ManualTokenStream("[[Bill], [John, James], [Dueber]]");
    StartsWithFilter filterOutput = new StartsWithFilter(ts);
    assertEquals("Bill1 John2 James2 Dueber3", TokenStreamTestHelpers.getTermsAsString(filterOutput));
  }

}
