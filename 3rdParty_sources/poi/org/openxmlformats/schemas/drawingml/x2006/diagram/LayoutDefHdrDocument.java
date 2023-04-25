/*
 * An XML document type.
 * Localname: layoutDefHdr
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.LayoutDefHdrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one layoutDefHdr(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public interface LayoutDefHdrDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.LayoutDefHdrDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "layoutdefhdr4c42doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "layoutDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader getLayoutDefHdr();

    /**
     * Sets the "layoutDefHdr" element
     */
    void setLayoutDefHdr(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader layoutDefHdr);

    /**
     * Appends and returns a new empty "layoutDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader addNewLayoutDefHdr();
}
