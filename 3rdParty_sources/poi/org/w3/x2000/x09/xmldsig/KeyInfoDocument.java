/*
 * An XML document type.
 * Localname: KeyInfo
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.KeyInfoDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one KeyInfo(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface KeyInfoDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.KeyInfoDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "keyinfoe194doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "KeyInfo" element
     */
    org.w3.x2000.x09.xmldsig.KeyInfoType getKeyInfo();

    /**
     * Sets the "KeyInfo" element
     */
    void setKeyInfo(org.w3.x2000.x09.xmldsig.KeyInfoType keyInfo);

    /**
     * Appends and returns a new empty "KeyInfo" element
     */
    org.w3.x2000.x09.xmldsig.KeyInfoType addNewKeyInfo();
}
