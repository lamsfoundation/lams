/*
 * An XML document type.
 * Localname: SignedInfo
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignedInfoDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SignedInfo(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface SignedInfoDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.SignedInfoDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signedinfoc0e5doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignedInfo" element
     */
    org.w3.x2000.x09.xmldsig.SignedInfoType getSignedInfo();

    /**
     * Sets the "SignedInfo" element
     */
    void setSignedInfo(org.w3.x2000.x09.xmldsig.SignedInfoType signedInfo);

    /**
     * Appends and returns a new empty "SignedInfo" element
     */
    org.w3.x2000.x09.xmldsig.SignedInfoType addNewSignedInfo();
}
