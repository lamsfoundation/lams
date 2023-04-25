/*
 * An XML document type.
 * Localname: from
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.FromDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one from(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing) element.
 *
 * This is a complex type.
 */
public interface FromDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.FromDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "from8892doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "from" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker getFrom();

    /**
     * Sets the "from" element
     */
    void setFrom(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker from);

    /**
     * Appends and returns a new empty "from" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker addNewFrom();
}
