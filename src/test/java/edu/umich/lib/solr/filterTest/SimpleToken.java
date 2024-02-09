package edu.umich.lib.solr.filterTest;

import java.util.Comparator;

class SimpleToken implements Comparable<SimpleToken> {
  public Integer position;
  public String text;

  public SimpleToken(String txt, Integer pos) {
    position = pos;
    text = txt;
  }

  @Override
  public int compareTo(SimpleToken other) {
    return position - other.position;
  }
}
