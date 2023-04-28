/*
 * XML Type:  CT_BlurEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_BlurEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBlurEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctblureffect2e33type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rad" attribute
     */
    long getRad();

    /**
     * Gets (as xml) the "rad" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetRad();

    /**
     * True if has "rad" attribute
     */
    boolean isSetRad();

    /**
     * Sets the "rad" attribute
     */
    void setRad(long rad);

    /**
     * Sets (as xml) the "rad" attribute
     */
    void xsetRad(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate rad);

    /**
     * Unsets the "rad" attribute
     */
    void unsetRad();

    /**
     * Gets the "grow" attribute
     */
    boolean getGrow();

    /**
     * Gets (as xml) the "grow" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetGrow();

    /**
     * True if has "grow" attribute
     */
    boolean isSetGrow();

    /**
     * Sets the "grow" attribute
     */
    void setGrow(boolean grow);

    /**
     * Sets (as xml) the "grow" attribute
     */
    void xsetGrow(org.apache.xmlbeans.XmlBoolean grow);

    /**
     * Unsets the "grow" attribute
     */
    void unsetGrow();
}
