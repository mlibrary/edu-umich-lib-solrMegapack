package edu.umich.lib.solr.anchoredSearch;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Munge the first token in the token stream (on query and index)
 * so any phrase match has to be anchored to the left.
 *
 * Factory for {@link ExactishFilter}-s. When added to the analysis
 * chain, it will cause phrase matches to the field to only match
 * if they start at the first token.
 *
 * NOTE that this actually changes the text of the first token(s), so
 * fields that include this filter are NOT suitable for generic searches.
 *
 * Example:
 *
 *     &lt;fieldType name="text_fullyanchored" class="solr.TextField"&gt;
 *         &lt;analyzer&gt;
 *              &lt;tokenizer class="solr.ICUTokenizerFactory"/&gt;
 *              &lt;filter class="solr.ICUFoldingFilterFactory"/&gt;
 *              &lt;filter class="edu.umich.lib.solr_fiilters.FullyAnchoredSearchFilterFactory"/&gt;
 *         &lt;/analyzer&gt;
 *     &lt;/fieldType&gt;
 */
public class ExactishFilterFactory extends TokenFilterFactory {
  public ExactishFilterFactory(Map<String, String> aMap) {
      super(aMap);
  }

  public ExactishFilterFactory() {
    this(new HashMap<String, String>());
  }

  @Override
    public ExactishFilter create(TokenStream aTokenStream) {
      return new ExactishFilter(aTokenStream);
  }

}