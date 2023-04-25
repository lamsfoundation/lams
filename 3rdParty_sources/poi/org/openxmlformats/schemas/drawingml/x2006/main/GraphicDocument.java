/*
 * An XML document type.
 * Localname: graphic
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.GraphicDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one graphic(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface GraphicDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.GraphicDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "graphic6f45doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "graphic" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject getGraphic();

    /**
     * Sets the "graphic" element
     */
    void setGraphic(org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject graphic);

    /**
     * Appends and returns a new empty "graphic" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject addNewGraphic();
}
