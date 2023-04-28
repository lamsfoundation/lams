/*
 * XML Type:  CT_ColorTransformHeaderLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeaderLst
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ColorTransformHeaderLst(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTColorTransformHeaderLst extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeaderLst> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcolortransformheaderlstd184type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "colorsDefHdr" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader> getColorsDefHdrList();

    /**
     * Gets array of all "colorsDefHdr" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader[] getColorsDefHdrArray();

    /**
     * Gets ith "colorsDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader getColorsDefHdrArray(int i);

    /**
     * Returns number of "colorsDefHdr" element
     */
    int sizeOfColorsDefHdrArray();

    /**
     * Sets array of all "colorsDefHdr" element
     */
    void setColorsDefHdrArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader[] colorsDefHdrArray);

    /**
     * Sets ith "colorsDefHdr" element
     */
    void setColorsDefHdrArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader colorsDefHdr);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "colorsDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader insertNewColorsDefHdr(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "colorsDefHdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader addNewColorsDefHdr();

    /**
     * Removes the ith "colorsDefHdr" element
     */
    void removeColorsDefHdr(int i);
}
