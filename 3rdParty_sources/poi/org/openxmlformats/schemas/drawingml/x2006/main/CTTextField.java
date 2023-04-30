/*
 * XML Type:  CT_TextField
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextField
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextField(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTextField extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTextField> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttextfield187etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties getRPr();

    /**
     * True if has "rPr" element
     */
    boolean isSetRPr();

    /**
     * Sets the "rPr" element
     */
    void setRPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties rPr);

    /**
     * Appends and returns a new empty "rPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties addNewRPr();

    /**
     * Unsets the "rPr" element
     */
    void unsetRPr();

    /**
     * Gets the "pPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties getPPr();

    /**
     * True if has "pPr" element
     */
    boolean isSetPPr();

    /**
     * Sets the "pPr" element
     */
    void setPPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties pPr);

    /**
     * Appends and returns a new empty "pPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties addNewPPr();

    /**
     * Unsets the "pPr" element
     */
    void unsetPPr();

    /**
     * Gets the "t" element
     */
    java.lang.String getT();

    /**
     * Gets (as xml) the "t" element
     */
    org.apache.xmlbeans.XmlString xgetT();

    /**
     * True if has "t" element
     */
    boolean isSetT();

    /**
     * Sets the "t" element
     */
    void setT(java.lang.String t);

    /**
     * Sets (as xml) the "t" element
     */
    void xsetT(org.apache.xmlbeans.XmlString t);

    /**
     * Unsets the "t" element
     */
    void unsetT();

    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid id);

    /**
     * Gets the "type" attribute
     */
    java.lang.String getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.apache.xmlbeans.XmlString xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(java.lang.String type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.apache.xmlbeans.XmlString type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();
}
