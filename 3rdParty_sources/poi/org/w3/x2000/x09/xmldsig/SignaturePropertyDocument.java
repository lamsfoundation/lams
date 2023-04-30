/*
 * An XML document type.
 * Localname: SignatureProperty
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignaturePropertyDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SignatureProperty(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface SignaturePropertyDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.SignaturePropertyDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signatureproperty7354doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignatureProperty" element
     */
    org.w3.x2000.x09.xmldsig.SignaturePropertyType getSignatureProperty();

    /**
     * Sets the "SignatureProperty" element
     */
    void setSignatureProperty(org.w3.x2000.x09.xmldsig.SignaturePropertyType signatureProperty);

    /**
     * Appends and returns a new empty "SignatureProperty" element
     */
    org.w3.x2000.x09.xmldsig.SignaturePropertyType addNewSignatureProperty();
}
