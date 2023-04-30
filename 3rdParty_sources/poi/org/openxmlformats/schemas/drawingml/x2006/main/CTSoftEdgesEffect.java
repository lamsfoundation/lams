/*
 * XML Type:  CT_SoftEdgesEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTSoftEdgesEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SoftEdgesEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSoftEdgesEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTSoftEdgesEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsoftedgeseffect096etype");
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
     * Sets the "rad" attribute
     */
    void setRad(long rad);

    /**
     * Sets (as xml) the "rad" attribute
     */
    void xsetRad(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate rad);
}
