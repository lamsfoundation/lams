/*
 * An XML document type.
 * Localname: colorsDefHdrLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.ColorsDefHdrLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one colorsDefHdrLst(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public interface ColorsDefHdrLstDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.ColorsDefHdrLstDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "colorsdefhdrlstc863doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "colorsDefHdrLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeaderLst getColorsDefHdrLst();

    /**
     * Sets the "colorsDefHdrLst" element
     */
    void setColorsDefHdrLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeaderLst colorsDefHdrLst);

    /**
     * Appends and returns a new empty "colorsDefHdrLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeaderLst addNewColorsDefHdrLst();
}
