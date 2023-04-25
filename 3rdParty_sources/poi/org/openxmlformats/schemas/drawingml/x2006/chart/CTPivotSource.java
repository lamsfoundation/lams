/*
 * XML Type:  CT_PivotSource
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotSource
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PivotSource(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTPivotSource extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotSource> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpivotsourcecb49type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "name" element
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName();

    /**
     * Sets the "name" element
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" element
     */
    void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name);

    /**
     * Gets the "fmtId" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getFmtId();

    /**
     * Sets the "fmtId" element
     */
    void setFmtId(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt fmtId);

    /**
     * Appends and returns a new empty "fmtId" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewFmtId();

    /**
     * Gets a List of "extLst" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList> getExtLstList();

    /**
     * Gets array of all "extLst" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList[] getExtLstArray();

    /**
     * Gets ith "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList getExtLstArray(int i);

    /**
     * Returns number of "extLst" element
     */
    int sizeOfExtLstArray();

    /**
     * Sets array of all "extLst" element
     */
    void setExtLstArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList[] extLstArray);

    /**
     * Sets ith "extLst" element
     */
    void setExtLstArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList insertNewExtLst(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst();

    /**
     * Removes the ith "extLst" element
     */
    void removeExtLst(int i);
}
