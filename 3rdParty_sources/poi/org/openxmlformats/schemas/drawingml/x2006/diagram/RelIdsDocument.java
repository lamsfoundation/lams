/*
 * An XML document type.
 * Localname: relIds
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.RelIdsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one relIds(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public interface RelIdsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.RelIdsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "relids971edoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "relIds" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTRelIds getRelIds();

    /**
     * Sets the "relIds" element
     */
    void setRelIds(org.openxmlformats.schemas.drawingml.x2006.diagram.CTRelIds relIds);

    /**
     * Appends and returns a new empty "relIds" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTRelIds addNewRelIds();
}
