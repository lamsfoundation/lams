/*
 * An XML document type.
 * Localname: CompleteCertificateRefs
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CompleteCertificateRefsDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one CompleteCertificateRefs(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface CompleteCertificateRefsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CompleteCertificateRefsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "completecertificaterefsa170doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "CompleteCertificateRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteCertificateRefsType getCompleteCertificateRefs();

    /**
     * Sets the "CompleteCertificateRefs" element
     */
    void setCompleteCertificateRefs(org.etsi.uri.x01903.v13.CompleteCertificateRefsType completeCertificateRefs);

    /**
     * Appends and returns a new empty "CompleteCertificateRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteCertificateRefsType addNewCompleteCertificateRefs();
}
