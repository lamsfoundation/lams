/*
 * An XML document type.
 * Localname: styleDefHdrLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.StyleDefHdrLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one styleDefHdrLst(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public interface StyleDefHdrLstDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.StyleDefHdrLstDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "styledefhdrlstd252doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "styleDefHdrLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst getStyleDefHdrLst();

    /**
     * Sets the "styleDefHdrLst" element
     */
    void setStyleDefHdrLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst styleDefHdrLst);

    /**
     * Appends and returns a new empty "styleDefHdrLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst addNewStyleDefHdrLst();
}
