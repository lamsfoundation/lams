/*
 * XML Type:  CT_AlphaModulateEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AlphaModulateEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAlphaModulateEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctalphamodulateeffect9b79type");
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
}
