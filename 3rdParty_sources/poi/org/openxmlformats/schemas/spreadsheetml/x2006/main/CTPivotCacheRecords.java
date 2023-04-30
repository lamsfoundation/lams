/*
 * XML Type:  CT_PivotCacheRecords
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PivotCacheRecords(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPivotCacheRecords extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpivotcacherecords5be1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "r" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRecord> getRList();

    /**
     * Gets array of all "r" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRecord[] getRArray();

    /**
     * Gets ith "r" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRecord getRArray(int i);

    /**
     * Returns number of "r" element
     */
    int sizeOfRArray();

    /**
     * Sets array of all "r" element
     */
    void setRArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRecord[] rArray);

    /**
     * Sets ith "r" element
     */
    void setRArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRecord r);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "r" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRecord insertNewR(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "r" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRecord addNewR();

    /**
     * Removes the ith "r" element
     */
    void removeR(int i);

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

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
