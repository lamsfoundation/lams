/*
 * An XML document type.
 * Localname: colorsDefHdr
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.ColorsDefHdrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one colorsDefHdr(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public interface ColorsDefHdrDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.ColorsDefHdrDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "colorsdefhdr593cdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "colorsDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader getColorsDefHdr();

    /**
     * Sets the "colorsDefHdr" element
     */
    void setColorsDefHdr(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader colorsDefHdr);

    /**
     * Appends and returns a new empty "colorsDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader addNewColorsDefHdr();
}
