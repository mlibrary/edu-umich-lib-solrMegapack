package edu.umich.lib.solr.pluginScaffold.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class SimpleFilterFactory<T> extends TokenFilterFactory {
  protected  Boolean echoInvalidInput;
  protected  Boolean keepOriginal;
  protected  Map<String, String> arguments;

  public SimpleFilterFactory(Map<String, String> args) throws IllegalArgumentException {
    super(args);
    arguments = args;
    arguments.keySet().stream().filter(key -> !validArguments().contains(key)).forEach(key -> {
      throw new IllegalArgumentException(
          "Key " + key
              + " is not legal for filter class "
              + this.getClass().toString()
              + ". Make sure to correctly override 'public List<String> validArguments()'"
              + " in your subclass of SimpleFilter.");
    });

    echoInvalidInput = getBoolean(arguments, "echoInvalidInput", false);
    keepOriginal = getBoolean(arguments, "keepOriginal", false);
  }

  public List<String> validArguments() {
    return new ArrayList<String>(Arrays.asList("echoInvalidInput", "keepOriginal"));
  }

  public abstract SimpleFilter create(TokenStream aStream);

}
