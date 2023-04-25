/*
 * XML Type:  CT_NameType
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NameType(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography).
 *
 * This is a complex type.
 */
public interface CTNameType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnametype5d07type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "NameList" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType getNameList();

    /**
     * Sets the "NameList" element
     */
    void setNameList(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType nameList);

    /**
     * Appends and returns a new empty "NameList" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType addNewNameList();
}
