/*
 * An XML document type.
 * Localname: layoutDefHdrLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.LayoutDefHdrLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one layoutDefHdrLst(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public interface LayoutDefHdrLstDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.LayoutDefHdrLstDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "layoutdefhdrlstaf9ddoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "layoutDefHdrLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst getLayoutDefHdrLst();

    /**
     * Sets the "layoutDefHdrLst" element
     */
    void setLayoutDefHdrLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst layoutDefHdrLst);

    /**
     * Appends and returns a new empty "layoutDefHdrLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst addNewLayoutDefHdrLst();
}
