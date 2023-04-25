/*
 * An XML document type.
 * Localname: SigningCertificate
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SigningCertificateDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SigningCertificate(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface SigningCertificateDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SigningCertificateDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signingcertificate6e56doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SigningCertificate" element
     */
    org.etsi.uri.x01903.v13.CertIDListType getSigningCertificate();

    /**
     * Sets the "SigningCertificate" element
     */
    void setSigningCertificate(org.etsi.uri.x01903.v13.CertIDListType signingCertificate);

    /**
     * Appends and returns a new empty "SigningCertificate" element
     */
    org.etsi.uri.x01903.v13.CertIDListType addNewSigningCertificate();
}
