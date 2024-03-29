package edu.umich.lib.solr.libraryIdentifier.isbn.analysis;

import edu.umich.lib.libraryIdentifier.isbn.ISBNNormalizer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Bill Dueber dueberb@umich.edu
 */
public class ISBNNormalizerFilter extends TokenFilter {
    /**
     * Logger used to log warnings.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ISBNNormalizerFilter.class);

    /**
     * The filter term that is a result of the conversion.
     */
    private final CharTermAttribute myTermAttribute =
            addAttribute(CharTermAttribute.class);

    /**
     * A Solr filter that parses ISO-639-1 and ISO-639-2 (ISBN) codes into English text
     * that can be used as a facet.
     *
     * @param aStream A {@link TokenStream} that parses streams with
     *                ISO-639-1 and ISO-639-2 codes
     */
    public ISBNNormalizerFilter(TokenStream aStream) {
        super(aStream);
    }

    /**
     * Increments and processes tokens in the ISO-639 code stream.
     *
     * @return True if a value is still available for processing in the token
     *         stream; otherwise, false
     */
    @Override
    public boolean incrementToken() throws IOException {
        if (!input.incrementToken()) {
            return false;
        }

        String t = myTermAttribute.toString();

        if (t != null && t.length() != 0) {
            try {
                myTermAttribute.setEmpty().append(ISBNNormalizer.normalize(t));
            } catch (IllegalArgumentException details) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(details.getMessage(), details);
                }
            }
        }

        return true;
    }


}
