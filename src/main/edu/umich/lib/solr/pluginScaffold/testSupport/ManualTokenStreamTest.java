package edu.umich.lib.solr.pluginScaffold.testSupport;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ManualTokenStreamTest {

  @Test
  public void singleAdd() throws IOException {
    ManualTokenStream ts = new ManualTokenStream();
    CharTermAttribute termAttribute = ts.addAttribute(CharTermAttribute.class);
    ts.add("Bill", 1);
    ts.incrementToken();
    assertEquals("Bill", termAttribute.toString());
  }

  @Test
  public void doubleAdd() throws IOException {
    ManualTokenStream ts = new ManualTokenStream();
    ts.add("Bill", 1);
    ts.add("Dueber", 2);
    assertArrayEquals(new String[]{"Bill", "Dueber"}, ts.terms());
  }

  @Test
  public void createWithString() throws IOException {
    ManualTokenStream ts = new ManualTokenStream("Bill");
    assertArrayEquals(new String[]{"Bill"}, ts.terms());
  }

  @Test
  public void createWithStrings() throws IOException {
    ManualTokenStream ts = new ManualTokenStream("Bill", "Dueber");
    assertArrayEquals(new String[]{"Bill", "Dueber"}, ts.terms());
  }

  @Test
  public void createWithList() throws IOException {
    ManualTokenStream ts = new ManualTokenStream(Arrays.asList("Bill", "Dueber"));
    assertArrayEquals(new String[]{"Bill", "Dueber"}, ts.terms());
  }

  @Test
  public void createWithArray() throws IOException {
    String[] tokens = {"Bill", "Dueber"};
    ManualTokenStream ts = new ManualTokenStream(tokens);
    assertArrayEquals(new String[]{"Bill", "Dueber"}, ts.terms());
  }

  @Test
  public void createWithMixed() throws IOException {
    ManualTokenStream ts = new ManualTokenStream(
        Arrays.asList("one",
            Arrays.asList("two", "three"),
            "four"));
    assertEquals("[one, [two, three], four]", ts.nestedTermsAsString());
  }
}
