/*
 * XML Type:  CT_AlphaOutsetEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaOutsetEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AlphaOutsetEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAlphaOutsetEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaOutsetEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctalphaoutseteffect8ab4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rad" attribute
     */
    java.lang.Object getRad();

    /**
     * Gets (as xml) the "rad" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetRad();

    /**
     * True if has "rad" attribute
     */
    boolean isSetRad();

    /**
     * Sets the "rad" attribute
     */
    void setRad(java.lang.Object rad);

    /**
     * Sets (as xml) the "rad" attribute
     */
    void xsetRad(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate rad);

    /**
     * Unsets the "rad" attribute
     */
    void unsetRad();
}
