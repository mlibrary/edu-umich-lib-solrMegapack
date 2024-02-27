package edu.umich.lib.solr.libraryIdentifier.lccn.analysis;

import edu.umich.lib.libraryIdentifier.lccn.LCCNNormalizer;
import edu.umich.lib.solr.pluginScaffold.analysis.SimpleFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dueberb
 * Date: 1/30/15
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */

public final class LCCNNormalizerFilter extends SimpleFilter {
  /**
   * A Solr filter that parses ISO-639-1 and ISO-639-2 codes into English text
   * that can be used as a facet.
   *
   * @param aStream A {@link TokenStream} that parses streams with
   *                ISO-639-1 and ISO-639-2 codes
   */
  public LCCNNormalizerFilter(TokenStream aStream) {
    super(aStream);
  }

  @Override
  public String munge(String str) {
    return LCCNNormalizer.normalize(str);
  }
}
