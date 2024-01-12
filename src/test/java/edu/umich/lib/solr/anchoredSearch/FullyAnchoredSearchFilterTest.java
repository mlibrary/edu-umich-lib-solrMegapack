package edu.umich.lib.solr.anchoredSearch;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FullyAnchoredSearchFilterTest {

  @Test
  public void testNested() throws IOException {
        ManualTokenStream ts = new ManualTokenStream();

    ts.add("Bill", 1);
    ts.add("John", 2);
    ts.add("James", 2);
    ts.add("Dueber", 3);

    FullyAnchoredSearchFilter ff = new FullyAnchoredSearchFilter(ts);
    List<String[]> terms = TokenStreamTestHelpers.get_nested_terms(ff);
    assertEquals("Bill1", terms.get(0)[0]);
    assertEquals("John2", terms.get(1)[0]);    
    assertEquals("James2", terms.get(1)[1]);
    assertEquals("Dueber300", terms.get(2)[0]);
  }

}
