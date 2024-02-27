package edu.umich.lib.solr.pluginScaffold.testSupport;

public class SimpleToken {
  public Integer position;
  public String text;

  public SimpleToken(String txt, Integer pos) {
    position = pos;
    text = txt;
  }
}
