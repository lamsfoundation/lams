/*
 * XML Type:  CT_EffectProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTEffectProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_EffectProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTEffectProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTEffectProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cteffectpropertiese23ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "effectLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList getEffectLst();

    /**
     * True if has "effectLst" element
     */
    boolean isSetEffectLst();

    /**
     * Sets the "effectLst" element
     */
    void setEffectLst(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList effectLst);

    /**
     * Appends and returns a new empty "effectLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList addNewEffectLst();

    /**
     * Unsets the "effectLst" element
     */
    void unsetEffectLst();

    /**
     * Gets the "effectDag" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer getEffectDag();

    /**
     * True if has "effectDag" element
     */
    boolean isSetEffectDag();

    /**
     * Sets the "effectDag" element
     */
    void setEffectDag(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer effectDag);

    /**
     * Appends and returns a new empty "effectDag" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer addNewEffectDag();

    /**
     * Unsets the "effectDag" element
     */
    void unsetEffectDag();
}
