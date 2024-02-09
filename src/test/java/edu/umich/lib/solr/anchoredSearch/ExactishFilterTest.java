package edu.umich.lib.solr.anchoredSearch;


import edu.umich.lib.solr.filterTest.TokenStreamTestHelpers;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.umich.lib.solr.filterTest.ManualTokenStream;


public class ExactishFilterTest {

  @Test
  public void testNested() throws IOException {
    ManualTokenStream ts = new ManualTokenStream("[[Bill], [John, James], [Dueber]]");
    ExactishFilter ff = new ExactishFilter(ts);
    assertEquals("[Bill1, [John2, James2], Dueber300]", TokenStreamTestHelpers.getNestedTermsAsString(ff));
  }

}
