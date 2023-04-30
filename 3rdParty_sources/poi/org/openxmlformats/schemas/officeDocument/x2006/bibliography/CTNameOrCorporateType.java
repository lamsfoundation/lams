/*
 * XML Type:  CT_NameOrCorporateType
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NameOrCorporateType(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography).
 *
 * This is a complex type.
 */
public interface CTNameOrCorporateType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnameorcorporatetype4185type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "NameList" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType getNameList();

    /**
     * True if has "NameList" element
     */
    boolean isSetNameList();

    /**
     * Sets the "NameList" element
     */
    void setNameList(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType nameList);

    /**
     * Appends and returns a new empty "NameList" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType addNewNameList();

    /**
     * Unsets the "NameList" element
     */
    void unsetNameList();

    /**
     * Gets the "Corporate" element
     */
    java.lang.String getCorporate();

    /**
     * Gets (as xml) the "Corporate" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetCorporate();

    /**
     * True if has "Corporate" element
     */
    boolean isSetCorporate();

    /**
     * Sets the "Corporate" element
     */
    void setCorporate(java.lang.String corporate);

    /**
     * Sets (as xml) the "Corporate" element
     */
    void xsetCorporate(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString corporate);

    /**
     * Unsets the "Corporate" element
     */
    void unsetCorporate();
}
