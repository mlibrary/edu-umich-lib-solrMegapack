package edu.umich.lib.solr.libraryIdentifier.isbn.analysis;

import edu.umich.lib.libraryIdentifier.isbn.ISBNNormalizer;
import edu.umich.lib.solr.pluginScaffold.analysis.SimpleFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author Bill Dueber dueberb@umich.edu
 */
public class ISBNNormalizerFilter extends SimpleFilter {

  public ISBNNormalizerFilter(TokenStream aStream) {
    this(aStream, false, false);
  }

  public ISBNNormalizerFilter(TokenStream aStream, Boolean echo, Boolean original) {
    super(aStream, echo, original);
  }


  @Override
  public String munge(String str) {
    return ISBNNormalizer.normalize(str);
  }


}
