/*
 * XML Type:  CT_PictureOptions
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureOptions
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PictureOptions(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTPictureOptions extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureOptions> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpictureoptions493ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "applyToFront" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getApplyToFront();

    /**
     * True if has "applyToFront" element
     */
    boolean isSetApplyToFront();

    /**
     * Sets the "applyToFront" element
     */
    void setApplyToFront(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean applyToFront);

    /**
     * Appends and returns a new empty "applyToFront" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewApplyToFront();

    /**
     * Unsets the "applyToFront" element
     */
    void unsetApplyToFront();

    /**
     * Gets the "applyToSides" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getApplyToSides();

    /**
     * True if has "applyToSides" element
     */
    boolean isSetApplyToSides();

    /**
     * Sets the "applyToSides" element
     */
    void setApplyToSides(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean applyToSides);

    /**
     * Appends and returns a new empty "applyToSides" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewApplyToSides();

    /**
     * Unsets the "applyToSides" element
     */
    void unsetApplyToSides();

    /**
     * Gets the "applyToEnd" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getApplyToEnd();

    /**
     * True if has "applyToEnd" element
     */
    boolean isSetApplyToEnd();

    /**
     * Sets the "applyToEnd" element
     */
    void setApplyToEnd(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean applyToEnd);

    /**
     * Appends and returns a new empty "applyToEnd" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewApplyToEnd();

    /**
     * Unsets the "applyToEnd" element
     */
    void unsetApplyToEnd();

    /**
     * Gets the "pictureFormat" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureFormat getPictureFormat();

    /**
     * True if has "pictureFormat" element
     */
    boolean isSetPictureFormat();

    /**
     * Sets the "pictureFormat" element
     */
    void setPictureFormat(org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureFormat pictureFormat);

    /**
     * Appends and returns a new empty "pictureFormat" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureFormat addNewPictureFormat();

    /**
     * Unsets the "pictureFormat" element
     */
    void unsetPictureFormat();

    /**
     * Gets the "pictureStackUnit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureStackUnit getPictureStackUnit();

    /**
     * True if has "pictureStackUnit" element
     */
    boolean isSetPictureStackUnit();

    /**
     * Sets the "pictureStackUnit" element
     */
    void setPictureStackUnit(org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureStackUnit pictureStackUnit);

    /**
     * Appends and returns a new empty "pictureStackUnit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureStackUnit addNewPictureStackUnit();

    /**
     * Unsets the "pictureStackUnit" element
     */
    void unsetPictureStackUnit();
}
