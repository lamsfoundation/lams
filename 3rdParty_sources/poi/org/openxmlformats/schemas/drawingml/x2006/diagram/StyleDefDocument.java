/*
 * An XML document type.
 * Localname: styleDef
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.StyleDefDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one styleDef(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public interface StyleDefDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.StyleDefDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "styledefb1c9doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "styleDef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinition getStyleDef();

    /**
     * Sets the "styleDef" element
     */
    void setStyleDef(org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinition styleDef);

    /**
     * Appends and returns a new empty "styleDef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinition addNewStyleDef();
}
