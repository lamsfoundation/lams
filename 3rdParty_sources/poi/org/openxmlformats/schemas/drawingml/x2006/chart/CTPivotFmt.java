/*
 * XML Type:  CT_PivotFmt
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PivotFmt(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTPivotFmt extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpivotfmt6bf1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "idx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getIdx();

    /**
     * Sets the "idx" element
     */
    void setIdx(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt idx);

    /**
     * Appends and returns a new empty "idx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewIdx();

    /**
     * Gets the "spPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties getSpPr();

    /**
     * True if has "spPr" element
     */
    boolean isSetSpPr();

    /**
     * Sets the "spPr" element
     */
    void setSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties spPr);

    /**
     * Appends and returns a new empty "spPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties addNewSpPr();

    /**
     * Unsets the "spPr" element
     */
    void unsetSpPr();

    /**
     * Gets the "txPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody getTxPr();

    /**
     * True if has "txPr" element
     */
    boolean isSetTxPr();

    /**
     * Sets the "txPr" element
     */
    void setTxPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody txPr);

    /**
     * Appends and returns a new empty "txPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody addNewTxPr();

    /**
     * Unsets the "txPr" element
     */
    void unsetTxPr();

    /**
     * Gets the "marker" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTMarker getMarker();

    /**
     * True if has "marker" element
     */
    boolean isSetMarker();

    /**
     * Sets the "marker" element
     */
    void setMarker(org.openxmlformats.schemas.drawingml.x2006.chart.CTMarker marker);

    /**
     * Appends and returns a new empty "marker" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTMarker addNewMarker();

    /**
     * Unsets the "marker" element
     */
    void unsetMarker();

    /**
     * Gets the "dLbl" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbl getDLbl();

    /**
     * True if has "dLbl" element
     */
    boolean isSetDLbl();

    /**
     * Sets the "dLbl" element
     */
    void setDLbl(org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbl dLbl);

    /**
     * Appends and returns a new empty "dLbl" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbl addNewDLbl();

    /**
     * Unsets the "dLbl" element
     */
    void unsetDLbl();

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
