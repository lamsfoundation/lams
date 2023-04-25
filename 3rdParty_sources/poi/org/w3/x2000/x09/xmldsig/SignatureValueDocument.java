/*
 * An XML document type.
 * Localname: SignatureValue
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignatureValueDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SignatureValue(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface SignatureValueDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.SignatureValueDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signaturevalueed56doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignatureValue" element
     */
    org.w3.x2000.x09.xmldsig.SignatureValueType getSignatureValue();

    /**
     * Sets the "SignatureValue" element
     */
    void setSignatureValue(org.w3.x2000.x09.xmldsig.SignatureValueType signatureValue);

    /**
     * Appends and returns a new empty "SignatureValue" element
     */
    org.w3.x2000.x09.xmldsig.SignatureValueType addNewSignatureValue();
}
