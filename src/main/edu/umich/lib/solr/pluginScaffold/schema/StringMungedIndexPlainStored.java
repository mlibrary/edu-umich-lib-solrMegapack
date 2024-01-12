package edu.umich.lib.solr.pluginScaffold.schema;

import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexableField;
import org.apache.solr.schema.SchemaField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class StringMungedIndexPlainStored extends StringMungedIndexMungedStored {
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private Boolean echoInvalidInput = false;

  @Override
  public IndexableField createField(SchemaField field, Object value) {

    if (!field.indexed() && !field.stored()) {
      if (log.isTraceEnabled())
        log.trace("Ignoring unindexed/unstored field: {}", field);
      return null;
    }

    String val = value.toString();


    /**
     * Bail if we were given null to begin with, or if the munged value is null and
     * we're not echoing invalid input.
     */

    if (val == null) return null;
    if (this.munged == null && !echoInvalidInput) return null;

    /**
     *  Now create the new field with the passed-in value, so the stored value is what was passed in
     *  and the indexed value is whatever munge returns.
     */

    org.apache.lucene.document.FieldType newType = new org.apache.lucene.document.FieldType();
    newType.setTokenized(true);
    newType.setStored(field.stored());
    newType.setIndexOptions(IndexOptions.DOCS);

    return createField(field.getName(), val, newType);
  }


}
