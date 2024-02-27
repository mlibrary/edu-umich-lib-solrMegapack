package edu.umich.lib.solr.pluginScaffold.testSupport;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TokenStreamTestableReaders {

   boolean incrementToken() throws IOException;
   CharTermAttribute getTermAttribute();

  PositionIncrementAttribute getPosIncrAtt();

  void reset() throws IOException;

  default ArrayList<SimpleToken> simpleTokens() throws IOException {
    ArrayList<SimpleToken> sts = new ArrayList<>();
    int currentTokenPosition = 0;
    while (this.incrementToken()) {
      PositionIncrementAttribute p = getPosIncrAtt();
      CharTermAttribute c = getTermAttribute();
      currentTokenPosition += p.getPositionIncrement();
      sts.add(new SimpleToken(getTermAttribute().toString(), currentTokenPosition));
    }
    return sts;
  }
  default String[] terms() throws IOException {
    return this.simpleTokens().stream().map(s -> s.text).toArray(String[]::new);
  }

  default String nestedTermsAsString() throws IOException {
    List<List<String>> nested = this.nestedTerms();
    List<String> rv = new ArrayList<>();
    for (List<String> tokens : nested) {
      if (tokens.size() == 1) {
        rv.add(tokens.get(0));
      } else {
        rv.add(termListToString(tokens));
      }
    }
    return termListToString(rv);
  }


  default List<List<String>> nestedTerms() throws IOException {
    ArrayList<SimpleToken> simpleTokens = this.simpleTokens();

    int lastPosition = simpleTokens.get(simpleTokens.size() - 1).position;
    List<List<String>> values = new ArrayList<>();

    // Add some empty arrays
    for (int i = 0; i < lastPosition; i++) {
      values.add(i, new ArrayList<String>());
    }
    simpleTokens.forEach(st -> {
      values.get(st.position - 1).add(st.text);
    });
    return values;
  }

  default String termListToString(List<String> tokens) {
    return "[" + String.join(", ", tokens) + "]";
  }
}
