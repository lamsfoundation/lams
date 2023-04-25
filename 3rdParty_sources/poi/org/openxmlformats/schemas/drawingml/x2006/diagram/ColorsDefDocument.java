/*
 * An XML document type.
 * Localname: colorsDef
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.ColorsDefDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one colorsDef(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public interface ColorsDefDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.ColorsDefDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "colorsdefc89adoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "colorsDef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransform getColorsDef();

    /**
     * Sets the "colorsDef" element
     */
    void setColorsDef(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransform colorsDef);

    /**
     * Appends and returns a new empty "colorsDef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransform addNewColorsDef();
}
