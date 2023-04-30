/*
 * An XML document type.
 * Localname: wpc
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.WpcDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one wpc(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing) element.
 *
 * This is a complex type.
 */
public interface WpcDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.WpcDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "wpc7c33doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "wpc" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingCanvas getWpc();

    /**
     * Sets the "wpc" element
     */
    void setWpc(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingCanvas wpc);

    /**
     * Appends and returns a new empty "wpc" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingCanvas addNewWpc();
}
