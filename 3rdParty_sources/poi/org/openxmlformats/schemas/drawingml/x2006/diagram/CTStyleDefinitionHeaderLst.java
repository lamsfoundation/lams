/*
 * XML Type:  CT_StyleDefinitionHeaderLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_StyleDefinitionHeaderLst(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTStyleDefinitionHeaderLst extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctstyledefinitionheaderlst1bb5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "styleDefHdr" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader> getStyleDefHdrList();

    /**
     * Gets array of all "styleDefHdr" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader[] getStyleDefHdrArray();

    /**
     * Gets ith "styleDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader getStyleDefHdrArray(int i);

    /**
     * Returns number of "styleDefHdr" element
     */
    int sizeOfStyleDefHdrArray();

    /**
     * Sets array of all "styleDefHdr" element
     */
    void setStyleDefHdrArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader[] styleDefHdrArray);

    /**
     * Sets ith "styleDefHdr" element
     */
    void setStyleDefHdrArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader styleDefHdr);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "styleDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader insertNewStyleDefHdr(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "styleDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader addNewStyleDefHdr();

    /**
     * Removes the ith "styleDefHdr" element
     */
    void removeStyleDefHdr(int i);
}
