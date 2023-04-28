/*
 * XML Type:  CT_DiagramDefinitionHeaderLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DiagramDefinitionHeaderLst(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTDiagramDefinitionHeaderLst extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdiagramdefinitionheaderlstc917type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "layoutDefHdr" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader> getLayoutDefHdrList();

    /**
     * Gets array of all "layoutDefHdr" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader[] getLayoutDefHdrArray();

    /**
     * Gets ith "layoutDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader getLayoutDefHdrArray(int i);

    /**
     * Returns number of "layoutDefHdr" element
     */
    int sizeOfLayoutDefHdrArray();

    /**
     * Sets array of all "layoutDefHdr" element
     */
    void setLayoutDefHdrArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader[] layoutDefHdrArray);

    /**
     * Sets ith "layoutDefHdr" element
     */
    void setLayoutDefHdrArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader layoutDefHdr);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "layoutDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader insertNewLayoutDefHdr(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "layoutDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader addNewLayoutDefHdr();

    /**
     * Removes the ith "layoutDefHdr" element
     */
    void removeLayoutDefHdr(int i);
}
