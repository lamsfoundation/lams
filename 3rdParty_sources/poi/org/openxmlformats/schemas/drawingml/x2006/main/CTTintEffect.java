/*
 * XML Type:  CT_TintEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TintEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTintEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttinteffect1ac7type");
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
     * Gets the "amt" attribute
     */
    java.lang.Object getAmt();

    /**
     * Gets (as xml) the "amt" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetAmt();

    /**
     * True if has "amt" attribute
     */
    boolean isSetAmt();

    /**
     * Sets the "amt" attribute
     */
    void setAmt(java.lang.Object amt);

    /**
     * Sets (as xml) the "amt" attribute
     */
    void xsetAmt(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage amt);

    /**
     * Unsets the "amt" attribute
     */
    void unsetAmt();
}
