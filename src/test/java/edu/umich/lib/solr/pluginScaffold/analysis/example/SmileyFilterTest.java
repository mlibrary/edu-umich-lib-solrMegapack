package edu.umich.lib.solr.pluginScaffold.analysis.example;

import edu.umich.lib.solr.pluginScaffold.testSupport.ManualTokenStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SmileyFilterTest {

  @Test
  public void testNoKeep() throws IOException {
    ManualTokenStream ts = new ManualTokenStream("hello", "there");
    SmileyFilter sm = new SmileyFilter(ts, false, false);
    assertEquals("[hell😀, there]", sm.nestedTermsAsString());
  }

  @Test
  public void testKeep() throws IOException {
    ManualTokenStream ts = new ManualTokenStream("hello", "there");
    SmileyFilter sm = new SmileyFilter(ts, false, true);
    assertEquals("[[hell😀, hello], there]", sm.nestedTermsAsString());
  }

}