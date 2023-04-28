/*
 * XML Type:  CT_Scaling
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Scaling(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTScaling extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctscaling1dfftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "logBase" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase getLogBase();

    /**
     * True if has "logBase" element
     */
    boolean isSetLogBase();

    /**
     * Sets the "logBase" element
     */
    void setLogBase(org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase logBase);

    /**
     * Appends and returns a new empty "logBase" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase addNewLogBase();

    /**
     * Unsets the "logBase" element
     */
    void unsetLogBase();

    /**
     * Gets the "orientation" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation getOrientation();

    /**
     * True if has "orientation" element
     */
    boolean isSetOrientation();

    /**
     * Sets the "orientation" element
     */
    void setOrientation(org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation orientation);

    /**
     * Appends and returns a new empty "orientation" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation addNewOrientation();

    /**
     * Unsets the "orientation" element
     */
    void unsetOrientation();

    /**
     * Gets the "max" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble getMax();

    /**
     * True if has "max" element
     */
    boolean isSetMax();

    /**
     * Sets the "max" element
     */
    void setMax(org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble max);

    /**
     * Appends and returns a new empty "max" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble addNewMax();

    /**
     * Unsets the "max" element
     */
    void unsetMax();

    /**
     * Gets the "min" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble getMin();

    /**
     * True if has "min" element
     */
    boolean isSetMin();

    /**
     * Sets the "min" element
     */
    void setMin(org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble min);

    /**
     * Appends and returns a new empty "min" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble addNewMin();

    /**
     * Unsets the "min" element
     */
    void unsetMin();

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
