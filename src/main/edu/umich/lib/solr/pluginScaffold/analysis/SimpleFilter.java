package edu.umich.lib.solr.pluginScaffold.analysis;

import edu.umich.lib.solr.pluginScaffold.testSupport.TokenStreamTestableReaders;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

public class SimpleFilter extends TokenFilter implements TokenStreamTestableReaders {

  public Map<String, String> arguments = new HashMap<>();

  private static final Logger LOGGER = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  /**
   * The filter term that is a result of the conversion.
   */
  private final CharTermAttribute termAttribute =
      addAttribute(CharTermAttribute.class);

  private AttributeSource.State savedTokenState = null;
  private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);


  // Know if something is labeled as a "keyword" -- a second copy of the token,
  // so you can mess with the first one and leave the keyword alone
  private final KeywordAttribute keywordAttr = addAttribute(KeywordAttribute.class);
  /**
   * If our munging function returns null, should we send along whatever the
   * input was instead?
   */

  public Boolean echoInvalidInput = false;
  public Boolean keepOriginal = false;

  // In their infinite wisdom all the get* methods in org.apache.lucene.analysis.util.AbstractAnalysisFactory
  // are protected, and hence unavailable here in the filter class, which is where it would be nice to
  // deal with them.

  // So, copy and paste to the resuce.


  public SimpleFilter(TokenStream aStream) {
    this(aStream, false, false);
  }

  public SimpleFilter(TokenStream aStream, Boolean invalid, Boolean original) {
    super(aStream);
    echoInvalidInput = invalid;
    keepOriginal = original;
  }

  public CharTermAttribute getTermAttribute() {
    return termAttribute;
  }

  public PositionIncrementAttribute getPosIncrAtt() {
    return posIncrAtt;
  }


  /**
   * The overridable "munge" method, which should change the input string in whatever
   * way you want, returning `null` if the input is invalid in some way.
   * <p>
   * Invalid input will be ignored or just echoed back as the indexed value,
   * depending on what `echoInvalidInput` is set to
   *
   * @return [String, null] The changed string, or null if it's illegal
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
  public final boolean incrementToken() throws IOException {
    // If the previously-eaten token saved the state, we restore to when
    // the original string was in there and put it at the same position

    if (savedTokenState != null) {         // Emit last token's type at the same position
      restoreState(savedTokenState);
      savedTokenState = null;
      String t = termAttribute.toString();
      termAttribute.setEmpty();
      termAttribute.append(t);
      posIncrAtt.setPositionIncrement(0);
      return true;
    }

    if (!input.incrementToken()) return false;
    if (keywordAttr.isKeyword()) return true;


    // Otherwise, munge the current token
    AttributeSource.State state = captureState(); // Capture the state with the un-munged token
    String t = termAttribute.toString(); // munge it and save
    String newStr = munge(t);

    // If newStr is neither null nor the same string (unchanged), keep it
    if (!((newStr == null) || (newStr.equals(t)))) {
      termAttribute.setEmpty();
      termAttribute.append(newStr);
    }

    if (emitOriginal(t, newStr)) {
      savedTokenState = state;
    } else {
      savedTokenState = null;
    }
    // Regardless, return true to say we're done with this one.
    return true;
  }

  private Boolean emitOriginal(String originalStr, String mungedStr) {
    if (originalStr.equals(mungedStr)) return false;
    if (echoInvalidInput && (mungedStr == null)) return true;
    if (keepOriginal) return true;
    return false;
  }


}
