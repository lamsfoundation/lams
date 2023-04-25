/*
 * XML Type:  CT_BlendEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTBlendEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_BlendEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBlendEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTBlendEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctblendeffect9173type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cont" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer getCont();

    /**
     * Sets the "cont" element
     */
    void setCont(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer cont);

    /**
     * Appends and returns a new empty "cont" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer addNewCont();

    /**
     * Gets the "blend" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STBlendMode.Enum getBlend();

    /**
     * Gets (as xml) the "blend" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STBlendMode xgetBlend();

    /**
     * Sets the "blend" attribute
     */
    void setBlend(org.openxmlformats.schemas.drawingml.x2006.main.STBlendMode.Enum blend);

    /**
     * Sets (as xml) the "blend" attribute
     */
    void xsetBlend(org.openxmlformats.schemas.drawingml.x2006.main.STBlendMode blend);
}
