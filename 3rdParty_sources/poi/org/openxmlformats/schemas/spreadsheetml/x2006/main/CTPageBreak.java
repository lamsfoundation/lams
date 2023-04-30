/*
 * XML Type:  CT_PageBreak
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PageBreak(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPageBreak extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpagebreakeb4ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "brk" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBreak> getBrkList();

    /**
     * Gets array of all "brk" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBreak[] getBrkArray();

    /**
     * Gets ith "brk" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBreak getBrkArray(int i);

    /**
     * Returns number of "brk" element
     */
    int sizeOfBrkArray();

    /**
     * Sets array of all "brk" element
     */
    void setBrkArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBreak[] brkArray);

    /**
     * Sets ith "brk" element
     */
    void setBrkArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBreak brk);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "brk" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBreak insertNewBrk(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "brk" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBreak addNewBrk();

    /**
     * Removes the ith "brk" element
     */
    void removeBrk(int i);

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

    /**
     * Gets the "manualBreakCount" attribute
     */
    long getManualBreakCount();

    /**
     * Gets (as xml) the "manualBreakCount" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetManualBreakCount();

    /**
     * True if has "manualBreakCount" attribute
     */
    boolean isSetManualBreakCount();

    /**
     * Sets the "manualBreakCount" attribute
     */
    void setManualBreakCount(long manualBreakCount);

    /**
     * Sets (as xml) the "manualBreakCount" attribute
     */
    void xsetManualBreakCount(org.apache.xmlbeans.XmlUnsignedInt manualBreakCount);

    /**
     * Unsets the "manualBreakCount" attribute
     */
    void unsetManualBreakCount();
}
