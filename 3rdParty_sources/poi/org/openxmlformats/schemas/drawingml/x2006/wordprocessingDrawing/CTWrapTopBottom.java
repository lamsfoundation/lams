/*
 * XML Type:  CT_WrapTopBottom
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWrapTopBottom
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WrapTopBottom(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public interface CTWrapTopBottom extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWrapTopBottom> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwraptopbottom5e13type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "effectExtent" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent getEffectExtent();

    /**
     * True if has "effectExtent" element
     */
    boolean isSetEffectExtent();

    /**
     * Sets the "effectExtent" element
     */
    void setEffectExtent(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent effectExtent);

    /**
     * Appends and returns a new empty "effectExtent" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent addNewEffectExtent();

    /**
     * Unsets the "effectExtent" element
     */
    void unsetEffectExtent();

    /**
     * Gets the "distT" attribute
     */
    long getDistT();

    /**
     * Gets (as xml) the "distT" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance xgetDistT();

    /**
     * True if has "distT" attribute
     */
    boolean isSetDistT();

    /**
     * Sets the "distT" attribute
     */
    void setDistT(long distT);

    /**
     * Sets (as xml) the "distT" attribute
     */
    void xsetDistT(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance distT);

    /**
     * Unsets the "distT" attribute
     */
    void unsetDistT();

    /**
     * Gets the "distB" attribute
     */
    long getDistB();

    /**
     * Gets (as xml) the "distB" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance xgetDistB();

    /**
     * True if has "distB" attribute
     */
    boolean isSetDistB();

    /**
     * Sets the "distB" attribute
     */
    void setDistB(long distB);

    /**
     * Sets (as xml) the "distB" attribute
     */
    void xsetDistB(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance distB);

    /**
     * Unsets the "distB" attribute
     */
    void unsetDistB();
}
