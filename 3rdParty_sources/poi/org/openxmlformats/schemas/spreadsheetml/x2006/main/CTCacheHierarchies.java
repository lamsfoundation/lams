/*
 * XML Type:  CT_CacheHierarchies
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchies
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CacheHierarchies(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCacheHierarchies extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchies> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcachehierarchies373atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cacheHierarchy" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy> getCacheHierarchyList();

    /**
     * Gets array of all "cacheHierarchy" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy[] getCacheHierarchyArray();

    /**
     * Gets ith "cacheHierarchy" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy getCacheHierarchyArray(int i);

    /**
     * Returns number of "cacheHierarchy" element
     */
    int sizeOfCacheHierarchyArray();

    /**
     * Sets array of all "cacheHierarchy" element
     */
    void setCacheHierarchyArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy[] cacheHierarchyArray);

    /**
     * Sets ith "cacheHierarchy" element
     */
    void setCacheHierarchyArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy cacheHierarchy);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cacheHierarchy" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy insertNewCacheHierarchy(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cacheHierarchy" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy addNewCacheHierarchy();

    /**
     * Removes the ith "cacheHierarchy" element
     */
    void removeCacheHierarchy(int i);

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
