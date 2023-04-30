/*
 * XML Type:  CT_Surface
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Surface(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTSurface extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsurface5a19type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "thickness" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTThickness getThickness();

    /**
     * True if has "thickness" element
     */
    boolean isSetThickness();

    /**
     * Sets the "thickness" element
     */
    void setThickness(org.openxmlformats.schemas.drawingml.x2006.chart.CTThickness thickness);

    /**
     * Appends and returns a new empty "thickness" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTThickness addNewThickness();

    /**
     * Unsets the "thickness" element
     */
    void unsetThickness();

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
     * Gets the "pictureOptions" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureOptions getPictureOptions();

    /**
     * True if has "pictureOptions" element
     */
    boolean isSetPictureOptions();

    /**
     * Sets the "pictureOptions" element
     */
    void setPictureOptions(org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureOptions pictureOptions);

    /**
     * Appends and returns a new empty "pictureOptions" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureOptions addNewPictureOptions();

    /**
     * Unsets the "pictureOptions" element
     */
    void unsetPictureOptions();

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
