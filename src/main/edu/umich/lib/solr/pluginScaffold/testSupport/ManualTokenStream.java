package edu.umich.lib.solr.pluginScaffold.testSupport;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.*;

public class ManualTokenStream extends TokenStream implements TokenStreamTestableReaders {

  List<SimpleToken> tokens = new ArrayList<>();
  public final CharTermAttribute termAttribute = addAttribute(CharTermAttribute.class);
  private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
  private Integer currentTokenPosition = 0;
  private Iterator<SimpleToken> iter = null;

  public ManualTokenStream() {
    super();
  }

  public ManualTokenStream(String... tokens) {
    super();
    for (int i = 0; i < tokens.length; i++) {
      add(tokens[i], i + 1);
    }
  }

  /**
   * Take a mixed list of strings and arrays/lists of strings to form the
   * tokenstream.
   *
   * This is unsafe in a wide variety of ways, but it's too useful to
   * ignore.
   *
   * Each element of tokens can be a String, String[], or List<String>
   */
  public ManualTokenStream(List<Object> tokens) {
    super();
    Object[] tokensArray = tokens.toArray();
    for (int i = 0; i < tokens.size(); i++) {
      int pos = i + 1;
      Object t = tokensArray[i];
      if (t instanceof String) {
        add((String) t, pos);
      }
      if (t instanceof List) {
        List<String> lst = Collections.unmodifiableList((List<String>) t);
        this.add(lst, pos);
      }
      if (t instanceof String[]) {
        List<String> lst = Arrays.asList((String[]) t);
        this.add(lst, pos);
      }
    }
  }

  public ManualTokenStream(Object... tokens) {
    this(Arrays.asList(tokens));
  }

  public ManualTokenStream add(String txt, Integer pos) {
    tokens.add(new SimpleToken(txt, pos));
    return this;
  }

  public ManualTokenStream add(List<String> strs, Integer pos) {
    for (String str : strs) add(str, pos);
    return this;
  }

  public CharTermAttribute getTermAttribute() {
    return termAttribute;
  }

  public PositionIncrementAttribute getPosIncrAtt() {
    return posIncrAtt;
  }


  @Override
  final public boolean incrementToken() throws IOException {
    if (iter == null) {
      iter = tokens.iterator();
      clearAttributes();
    }

    if (!iter.hasNext()) {
      return false;
    }

    SimpleToken t = iter.next();
    termAttribute.setEmpty().append(t.text);
    if (currentTokenPosition.equals(t.position)) {
      posIncrAtt.setPositionIncrement(0);
    } else {
      posIncrAtt.setPositionIncrement(1);
    }
    currentTokenPosition = t.position;
    return true;
  }

}

