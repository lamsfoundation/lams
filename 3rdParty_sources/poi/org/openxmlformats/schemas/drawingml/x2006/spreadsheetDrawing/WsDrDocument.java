/*
 * An XML document type.
 * Localname: wsDr
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.WsDrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one wsDr(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing) element.
 *
 * This is a complex type.
 */
public interface WsDrDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.WsDrDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "wsdrd172doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "wsDr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing getWsDr();

    /**
     * Sets the "wsDr" element
     */
    void setWsDr(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing wsDr);

    /**
     * Appends and returns a new empty "wsDr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing addNewWsDr();
}
