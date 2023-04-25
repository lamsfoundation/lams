/*
 * An XML document type.
 * Localname: SignaturePolicyIdentifier
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignaturePolicyIdentifierDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SignaturePolicyIdentifier(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface SignaturePolicyIdentifierDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignaturePolicyIdentifierDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signaturepolicyidentifier90c5doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignaturePolicyIdentifier" element
     */
    org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType getSignaturePolicyIdentifier();

    /**
     * Sets the "SignaturePolicyIdentifier" element
     */
    void setSignaturePolicyIdentifier(org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType signaturePolicyIdentifier);

    /**
     * Appends and returns a new empty "SignaturePolicyIdentifier" element
     */
    org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType addNewSignaturePolicyIdentifier();
}
