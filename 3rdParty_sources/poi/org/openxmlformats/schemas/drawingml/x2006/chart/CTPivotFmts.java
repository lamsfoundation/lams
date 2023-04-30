/*
 * XML Type:  CT_PivotFmts
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmts
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PivotFmts(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTPivotFmts extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmts> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpivotfmts863etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "pivotFmt" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt> getPivotFmtList();

    /**
     * Gets array of all "pivotFmt" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt[] getPivotFmtArray();

    /**
     * Gets ith "pivotFmt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt getPivotFmtArray(int i);

    /**
     * Returns number of "pivotFmt" element
     */
    int sizeOfPivotFmtArray();

    /**
     * Sets array of all "pivotFmt" element
     */
    void setPivotFmtArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt[] pivotFmtArray);

    /**
     * Sets ith "pivotFmt" element
     */
    void setPivotFmtArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt pivotFmt);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pivotFmt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt insertNewPivotFmt(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "pivotFmt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt addNewPivotFmt();

    /**
     * Removes the ith "pivotFmt" element
     */
    void removePivotFmt(int i);
}
