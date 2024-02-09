package edu.umich.lib.solr.filterTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.yaml.*;

/**
 * Convenient (?) ways to turn a list-of-list-of-strings into
 * a token stream, suitable for sending through a filter
 */
public class ManualTokenStream extends TokenStream {

  public List<SimpleToken> simpleTokens = new ArrayList<SimpleToken>();
  public TypeReference<List<List<String>>> lolosType = new TypeReference<List<List<String>>>() {
  };
  public YAMLMapper mapper = new YAMLMapper();
  public final CharTermAttribute termAttribute = addAttribute(CharTermAttribute.class);
  private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
  private Integer currentTokenPosition = 0;
  private Iterator<SimpleToken> iter = null;

  public Integer nextPosition() {
    if (simpleTokens.isEmpty()) {
      return 1;
    } else {
      return simpleTokens.get(simpleTokens.size() - 1).position + 1;
    }
  }

  public ManualTokenStream add(String txt, Integer pos) {
    simpleTokens.add(new SimpleToken(txt, pos));
    return this;
  }

  public ManualTokenStream add(String txt) {
    simpleTokens.add(new SimpleToken(txt, this.nextPosition()));
    return this;
  }

  public ManualTokenStream add(List<String> strings) {
    Integer nextPos = nextPosition();
    for (String s: strings) {
      this.add(s, nextPos);
    }
    return this;
  }

  public ManualTokenStream(String jsonOrYAMLString) throws JsonProcessingException {
    List<List<String>> yaml = mapper.readValue(jsonOrYAMLString, lolosType);
    Integer position = 0;
    for (List<String> lst : yaml) {
      position++;
      for (String token : lst) {
        simpleTokens.add(new SimpleToken(token, position));
      }
    }
  }

  public ManualTokenStream(String... tokens)  {
    Integer pos = 0;
    for (String token: tokens) {
      pos++;
      simpleTokens.add(new SimpleToken(token, pos));
    }
  }

  @Override
  final public boolean incrementToken() throws IOException {
    if (iter == null) {
      iter = simpleTokens.iterator();
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