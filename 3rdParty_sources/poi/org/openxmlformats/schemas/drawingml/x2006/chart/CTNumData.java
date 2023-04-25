/*
 * XML Type:  CT_NumData
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NumData(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTNumData extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnumdata4f16type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "formatCode" element
     */
    java.lang.String getFormatCode();

    /**
     * Gets (as xml) the "formatCode" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetFormatCode();

    /**
     * True if has "formatCode" element
     */
    boolean isSetFormatCode();

    /**
     * Sets the "formatCode" element
     */
    void setFormatCode(java.lang.String formatCode);

    /**
     * Sets (as xml) the "formatCode" element
     */
    void xsetFormatCode(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring formatCode);

    /**
     * Unsets the "formatCode" element
     */
    void unsetFormatCode();

    /**
     * Gets the "ptCount" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getPtCount();

    /**
     * True if has "ptCount" element
     */
    boolean isSetPtCount();

    /**
     * Sets the "ptCount" element
     */
    void setPtCount(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt ptCount);

    /**
     * Appends and returns a new empty "ptCount" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewPtCount();

    /**
     * Unsets the "ptCount" element
     */
    void unsetPtCount();

    /**
     * Gets a List of "pt" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTNumVal> getPtList();

    /**
     * Gets array of all "pt" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumVal[] getPtArray();

    /**
     * Gets ith "pt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumVal getPtArray(int i);

    /**
     * Returns number of "pt" element
     */
    int sizeOfPtArray();

    /**
     * Sets array of all "pt" element
     */
    void setPtArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumVal[] ptArray);

    /**
     * Sets ith "pt" element
     */
    void setPtArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTNumVal pt);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumVal insertNewPt(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "pt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumVal addNewPt();

    /**
     * Removes the ith "pt" element
     */
    void removePt(int i);

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}
