package edu.umich.lib.solr.pluginScaffold.analysis.example;

import edu.umich.lib.solr.pluginScaffold.schema.StringMungedIndexMungedStored;

public class SmileyIndexedAndStored extends StringMungedIndexMungedStored {

  @Override
  public String munge(String str) {
    return str.replaceAll("[Oo]", "😀");
  }
}
