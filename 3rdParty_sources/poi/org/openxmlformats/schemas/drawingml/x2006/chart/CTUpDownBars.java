/*
 * XML Type:  CT_UpDownBars
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_UpDownBars(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTUpDownBars extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctupdownbarsd79ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "gapWidth" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTGapAmount getGapWidth();

    /**
     * True if has "gapWidth" element
     */
    boolean isSetGapWidth();

    /**
     * Sets the "gapWidth" element
     */
    void setGapWidth(org.openxmlformats.schemas.drawingml.x2006.chart.CTGapAmount gapWidth);

    /**
     * Appends and returns a new empty "gapWidth" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTGapAmount addNewGapWidth();

    /**
     * Unsets the "gapWidth" element
     */
    void unsetGapWidth();

    /**
     * Gets the "upBars" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar getUpBars();

    /**
     * True if has "upBars" element
     */
    boolean isSetUpBars();

    /**
     * Sets the "upBars" element
     */
    void setUpBars(org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar upBars);

    /**
     * Appends and returns a new empty "upBars" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar addNewUpBars();

    /**
     * Unsets the "upBars" element
     */
    void unsetUpBars();

    /**
     * Gets the "downBars" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar getDownBars();

    /**
     * True if has "downBars" element
     */
    boolean isSetDownBars();

    /**
     * Sets the "downBars" element
     */
    void setDownBars(org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar downBars);

    /**
     * Appends and returns a new empty "downBars" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar addNewDownBars();

    /**
     * Unsets the "downBars" element
     */
    void unsetDownBars();

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
