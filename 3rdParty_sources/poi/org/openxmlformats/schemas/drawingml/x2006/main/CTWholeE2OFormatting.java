/*
 * XML Type:  CT_WholeE2oFormatting
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WholeE2oFormatting(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTWholeE2OFormatting extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwholee2oformattingdc8btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ln" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getLn();

    /**
     * True if has "ln" element
     */
    boolean isSetLn();

    /**
     * Sets the "ln" element
     */
    void setLn(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties ln);

    /**
     * Appends and returns a new empty "ln" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewLn();

    /**
     * Unsets the "ln" element
     */
    void unsetLn();

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
