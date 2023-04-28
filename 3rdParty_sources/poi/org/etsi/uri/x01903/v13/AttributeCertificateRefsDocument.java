/*
 * An XML document type.
 * Localname: AttributeCertificateRefs
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.AttributeCertificateRefsDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one AttributeCertificateRefs(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface AttributeCertificateRefsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.AttributeCertificateRefsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "attributecertificaterefsac3fdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "AttributeCertificateRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteCertificateRefsType getAttributeCertificateRefs();

    /**
     * Sets the "AttributeCertificateRefs" element
     */
    void setAttributeCertificateRefs(org.etsi.uri.x01903.v13.CompleteCertificateRefsType attributeCertificateRefs);

    /**
     * Appends and returns a new empty "AttributeCertificateRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteCertificateRefsType addNewAttributeCertificateRefs();
}
