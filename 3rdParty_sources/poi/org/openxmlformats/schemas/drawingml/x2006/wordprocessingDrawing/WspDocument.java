/*
 * An XML document type.
 * Localname: wsp
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.WspDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one wsp(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing) element.
 *
 * This is a complex type.
 */
public interface WspDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.WspDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "wsp6089doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "wsp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape getWsp();

    /**
     * Sets the "wsp" element
     */
    void setWsp(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape wsp);

    /**
     * Appends and returns a new empty "wsp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape addNewWsp();
}
