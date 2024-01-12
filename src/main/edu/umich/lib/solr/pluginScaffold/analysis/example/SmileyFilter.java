package edu.umich.lib.solr.pluginScaffold.analysis.example;

import edu.umich.lib.solr.pluginScaffold.analysis.SimpleFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * A subclass of SimpleFilter needs to have a constructor with at
 * least the two arguments (aStream, echoInvalidInput) that calls `super`,
 * and an implementation of `munge` to take the input string and turn it into whatever you
 * actually want indexed.
 */

public class SmileyFilter extends SimpleFilter {

  public SmileyFilter(TokenStream aStream, Boolean echoInvalidInput) {
    super(aStream, echoInvalidInput);
  }



  @Override
  public String munge(String str) {
    return str.replaceAll("[Oo]", "😀");
  }

}
