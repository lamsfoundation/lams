/*
 * XML Type:  CT_LegendEntry
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTLegendEntry
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_LegendEntry(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTLegendEntry extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTLegendEntry> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlegendentrya8e1type");
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
     * Gets the "delete" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getDelete();

    /**
     * True if has "delete" element
     */
    boolean isSetDelete();

    /**
     * Sets the "delete" element
     */
    void setDelete(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean delete);

    /**
     * Appends and returns a new empty "delete" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewDelete();

    /**
     * Unsets the "delete" element
     */
    void unsetDelete();

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
