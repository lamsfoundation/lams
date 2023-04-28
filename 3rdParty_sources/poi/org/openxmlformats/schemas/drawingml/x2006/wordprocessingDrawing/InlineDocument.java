/*
 * An XML document type.
 * Localname: inline
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.InlineDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one inline(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing) element.
 *
 * This is a complex type.
 */
public interface InlineDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.InlineDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "inline3252doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "inline" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline getInline();

    /**
     * Sets the "inline" element
     */
    void setInline(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline inline);

    /**
     * Appends and returns a new empty "inline" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline addNewInline();
}
