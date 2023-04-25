/*
 * An XML document type.
 * Localname: styleDefHdr
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.StyleDefHdrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one styleDefHdr(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public interface StyleDefHdrDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.StyleDefHdrDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "styledefhdr2daddoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "styleDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader getStyleDefHdr();

    /**
     * Sets the "styleDefHdr" element
     */
    void setStyleDefHdr(org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader styleDefHdr);

    /**
     * Appends and returns a new empty "styleDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader addNewStyleDefHdr();
}
