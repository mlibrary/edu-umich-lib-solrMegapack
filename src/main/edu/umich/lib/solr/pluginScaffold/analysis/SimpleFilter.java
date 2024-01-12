package edu.umich.lib.solr.pluginScaffold.analysis;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class SimpleFilter extends TokenFilter {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  /**
   * The filter term that is a result of the conversion.
   */
  private final CharTermAttribute myTermAttribute =
      addAttribute(CharTermAttribute.class);

  /**
   * If our munging function returns null, should we send along whatever the
   * input was instead?
   */

  private Boolean echoInvalidInput;

  public SimpleFilter(TokenStream aStream, Boolean echoInvalidInput) {
    super(aStream);
    this.echoInvalidInput = echoInvalidInput;
  }
  public SimpleFilter(TokenStream aStream) {
    this(aStream, false);
  }


  /**
   * The overridable "munge" method, which should change the input string in whatever
   * way you want, returning `null` if the input is invalid in some way.
   *
   * Invalid input will be ignored or just echoed back as the indexed value,
   * depending on what `echoInvalidInput` is set to
   */

  public String munge(String input) {
    return input;
  }

  /**
   * Increments and processes tokens in the ISO-639 code stream.
   *
   * @return True if a value is still available for processing in the token
   * stream; otherwise, false
   */
  @Override
  public boolean incrementToken() throws IOException {
    if (!input.incrementToken()) {
      return false;
    }

    String t = myTermAttribute.toString();
    if (t != null && t.length() != 0) {
      try {
        myTermAttribute.setEmpty();
        String newStr = munge(t);
        if (newStr == null) {
          if (echoInvalidInput) {
            myTermAttribute.append(t);
          } else {
            return false;
          }
        } else {
          myTermAttribute.append(newStr);
        }
      } catch (IllegalArgumentException details) {
        if (LOGGER.isInfoEnabled()) {
          LOGGER.info(details.getMessage(), details);
        }
      }
    }

    return true;
  }
}
