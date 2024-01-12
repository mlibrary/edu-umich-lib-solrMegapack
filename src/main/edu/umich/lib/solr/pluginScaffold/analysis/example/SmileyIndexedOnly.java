package edu.umich.lib.solr.pluginScaffold.analysis.example;

import edu.umich.lib.solr.pluginScaffold.schema.StringMungedIndexPlainStored;

public class SmileyIndexedOnly extends StringMungedIndexPlainStored {

  @Override
  public String munge(String str) {
    return str.replaceAll("[Oo]", "😀");
  }

}
