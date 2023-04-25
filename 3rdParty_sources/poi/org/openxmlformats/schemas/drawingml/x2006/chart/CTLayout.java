/*
 * XML Type:  CT_Layout
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Layout(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTLayout extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlayout3192type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "manualLayout" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout getManualLayout();

    /**
     * True if has "manualLayout" element
     */
    boolean isSetManualLayout();

    /**
     * Sets the "manualLayout" element
     */
    void setManualLayout(org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout manualLayout);

    /**
     * Appends and returns a new empty "manualLayout" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout addNewManualLayout();

    /**
     * Unsets the "manualLayout" element
     */
    void unsetManualLayout();

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
