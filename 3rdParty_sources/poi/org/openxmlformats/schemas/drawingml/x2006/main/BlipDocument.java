/*
 * An XML document type.
 * Localname: blip
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.BlipDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one blip(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface BlipDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.BlipDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "blipffa0doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "blip" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBlip getBlip();

    /**
     * Sets the "blip" element
     */
    void setBlip(org.openxmlformats.schemas.drawingml.x2006.main.CTBlip blip);

    /**
     * Appends and returns a new empty "blip" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBlip addNewBlip();
}
