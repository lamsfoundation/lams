/*
 * XML Type:  CT_HSLEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_HSLEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTHSLEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cthsleffectdca3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "hue" attribute
     */
    int getHue();

    /**
     * Gets (as xml) the "hue" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle xgetHue();

    /**
     * True if has "hue" attribute
     */
    boolean isSetHue();

    /**
     * Sets the "hue" attribute
     */
    void setHue(int hue);

    /**
     * Sets (as xml) the "hue" attribute
     */
    void xsetHue(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle hue);

    /**
     * Unsets the "hue" attribute
     */
    void unsetHue();

    /**
     * Gets the "sat" attribute
     */
    java.lang.Object getSat();

    /**
     * Gets (as xml) the "sat" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetSat();

    /**
     * True if has "sat" attribute
     */
    boolean isSetSat();

    /**
     * Sets the "sat" attribute
     */
    void setSat(java.lang.Object sat);

    /**
     * Sets (as xml) the "sat" attribute
     */
    void xsetSat(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage sat);

    /**
     * Unsets the "sat" attribute
     */
    void unsetSat();

    /**
     * Gets the "lum" attribute
     */
    java.lang.Object getLum();

    /**
     * Gets (as xml) the "lum" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetLum();

    /**
     * True if has "lum" attribute
     */
    boolean isSetLum();

    /**
     * Sets the "lum" attribute
     */
    void setLum(java.lang.Object lum);

    /**
     * Sets (as xml) the "lum" attribute
     */
    void xsetLum(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage lum);

    /**
     * Unsets the "lum" attribute
     */
    void unsetLum();
}
