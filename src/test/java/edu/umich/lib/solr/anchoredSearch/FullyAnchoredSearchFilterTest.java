package edu.umich.lib.solr.anchoredSearch;


import edu.umich.lib.solr.pluginScaffold.testSupport.ManualTokenStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FullyAnchoredSearchFilterTest {

  @Test
  public void testNested() throws IOException {
        ManualTokenStream ts = new ManualTokenStream("Bill",
            Arrays.asList("John", "James"),
            "Dueber");

    FullyAnchoredSearchFilter ff = new FullyAnchoredSearchFilter(ts);
    String terms = ff.nestedTermsAsString();
    assertEquals("[Bill1, [John2, James2], Dueber300]", terms);
  }
}
