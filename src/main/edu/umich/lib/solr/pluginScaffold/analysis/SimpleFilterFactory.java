package edu.umich.lib.solr.pluginScaffold.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.util.Map;

public class SimpleFilterFactory extends TokenFilterFactory {
  private Boolean echoInvalidInput;

  public SimpleFilterFactory(Map<String, String> args) {
    super(args);
    echoInvalidInput = getBoolean(args, "echoInvalidInput", false);
  }

  public Boolean getEchoInvalidInput() {
    return echoInvalidInput;
  }

  public SimpleFilter create(TokenStream input) {
    return new SimpleFilter(input, echoInvalidInput);
  }

}
