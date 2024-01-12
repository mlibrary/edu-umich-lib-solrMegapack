package edu.umich.lib.solr.pluginScaffold.schema;

import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.StrField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

public class StringMungedIndexMungedStored extends StrField {

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  protected Boolean echoInvalidInput = false;
  public String munged;

  protected void init(IndexSchema schema, Map<String, String> args) {
    super.init(schema, args);
    String p = args.remove("echoInvalidInput");
    if (p != null) {
      this.echoInvalidInput = Boolean.parseBoolean(p);
    }
  }

  public String munge(String str) {
    return str;
  }

  @Override
  public String toInternal(String val) {
    munged = munge(val);
    if (munged == null && echoInvalidInput) {
      munged = val;
    }
    return munged;
  }

}
