/*
 * XML Type:  CT_CacheFields
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CacheFields(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCacheFields extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcachefieldsf5fatype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cacheField" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheField> getCacheFieldList();

    /**
     * Gets array of all "cacheField" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheField[] getCacheFieldArray();

    /**
     * Gets ith "cacheField" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheField getCacheFieldArray(int i);

    /**
     * Returns number of "cacheField" element
     */
    int sizeOfCacheFieldArray();

    /**
     * Sets array of all "cacheField" element
     */
    void setCacheFieldArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheField[] cacheFieldArray);

    /**
     * Sets ith "cacheField" element
     */
    void setCacheFieldArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheField cacheField);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cacheField" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheField insertNewCacheField(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cacheField" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheField addNewCacheField();

    /**
     * Removes the ith "cacheField" element
     */
    void removeCacheField(int i);

    /**
     * Gets the "count" attribute
     */
    long getCount();

    /**
     * Gets (as xml) the "count" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCount();

    /**
     * True if has "count" attribute
     */
    boolean isSetCount();

    /**
     * Sets the "count" attribute
     */
    void setCount(long count);

    /**
     * Sets (as xml) the "count" attribute
     */
    void xsetCount(org.apache.xmlbeans.XmlUnsignedInt count);

    /**
     * Unsets the "count" attribute
     */
    void unsetCount();
}
