/*
 * An XML document type.
 * Localname: SignatureProperties
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignaturePropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SignatureProperties(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface SignaturePropertiesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.SignaturePropertiesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signatureproperties8af6doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignatureProperties" element
     */
    org.w3.x2000.x09.xmldsig.SignaturePropertiesType getSignatureProperties();

    /**
     * Sets the "SignatureProperties" element
     */
    void setSignatureProperties(org.w3.x2000.x09.xmldsig.SignaturePropertiesType signatureProperties);

    /**
     * Appends and returns a new empty "SignatureProperties" element
     */
    org.w3.x2000.x09.xmldsig.SignaturePropertiesType addNewSignatureProperties();
}
