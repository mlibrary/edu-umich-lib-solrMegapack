package edu.umich.lib.solr.filterTest;


import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TokenStreamTestHelpers {

    /*
  helper method to get simpletokens for any given TokenStream
   */

  private static final String[] EMPTY_ARRAY = {};

  public static List<SimpleToken> getSimpletokens(TokenStream ts) throws IOException {
    CharTermAttribute ta = ts.addAttribute(CharTermAttribute.class);
    PositionIncrementAttribute pia = ts.addAttribute(PositionIncrementAttribute.class);

    ArrayList<SimpleToken> sts = new ArrayList<>();
    int currentTokenPosition = 0;
    while (ts.incrementToken()) {
      currentTokenPosition += pia.getPositionIncrement();
      sts.add(new SimpleToken(ta.toString(), currentTokenPosition));
    }
    sts.sort(null);
    return sts;
  }

  public static List<String> getTerms(TokenStream ts) throws IOException {
    List<String> output = new ArrayList<String>();
    List<SimpleToken> simpletokens = getSimpletokens(ts);
    return simpletokens.stream().map(t -> t.text).collect(Collectors.toList());
  }

  public static String getTermsAsString(TokenStream ts) throws IOException {
    return String.join(" ", getTerms(ts));
  }

  public static List<List<String>> getNestedTerms(TokenStream ts) throws IOException {
    List<SimpleToken> tokens = getSimpletokens(ts);
    tokens.sort(null);
    ArrayList<List<String>> values = new ArrayList<>();
    tokens.forEach(st -> {
      if (values.size() < st.position) {
        values.add(new ArrayList<>());
      }
      values.get(st.position - 1).add(st.text);
    });
    return values;
  }

  public static String tokenListToString(List<String> lst) {
    if (lst.size() == 1) {
      return lst.get(0);
    } else {
      return "[" + String.join(", ", lst) + "]";
    }
  }

  public static String getNestedTermsAsString(TokenStream ts) throws IOException {
    List<String> output = new ArrayList();
    getNestedTerms(ts).forEach(lst -> {
      output.add(tokenListToString(lst));
    });
    return "[" + String.join(", ", output) + "]";
  }
}
