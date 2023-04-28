/*
 * An XML document type.
 * Localname: layoutDef
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.LayoutDefDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one layoutDef(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public interface LayoutDefDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.LayoutDefDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "layoutdef2354doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "layoutDef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition getLayoutDef();

    /**
     * Sets the "layoutDef" element
     */
    void setLayoutDef(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition layoutDef);

    /**
     * Appends and returns a new empty "layoutDef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition addNewLayoutDef();
}
