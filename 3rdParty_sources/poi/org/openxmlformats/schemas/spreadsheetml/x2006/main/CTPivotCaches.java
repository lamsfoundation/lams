/*
 * XML Type:  CT_PivotCaches
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCaches
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PivotCaches(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPivotCaches extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCaches> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpivotcaches4f32type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "pivotCache" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache> getPivotCacheList();

    /**
     * Gets array of all "pivotCache" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache[] getPivotCacheArray();

    /**
     * Gets ith "pivotCache" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache getPivotCacheArray(int i);

    /**
     * Returns number of "pivotCache" element
     */
    int sizeOfPivotCacheArray();

    /**
     * Sets array of all "pivotCache" element
     */
    void setPivotCacheArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache[] pivotCacheArray);

    /**
     * Sets ith "pivotCache" element
     */
    void setPivotCacheArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache pivotCache);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pivotCache" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache insertNewPivotCache(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "pivotCache" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache addNewPivotCache();

    /**
     * Removes the ith "pivotCache" element
     */
    void removePivotCache(int i);
}
