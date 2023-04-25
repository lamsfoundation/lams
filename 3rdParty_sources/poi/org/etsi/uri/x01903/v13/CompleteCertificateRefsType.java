/*
 * XML Type:  CompleteCertificateRefsType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CompleteCertificateRefsType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CompleteCertificateRefsType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CompleteCertificateRefsType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CompleteCertificateRefsType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "completecertificaterefstype07datype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "CertRefs" element
     */
    org.etsi.uri.x01903.v13.CertIDListType getCertRefs();

    /**
     * Sets the "CertRefs" element
     */
    void setCertRefs(org.etsi.uri.x01903.v13.CertIDListType certRefs);

    /**
     * Appends and returns a new empty "CertRefs" element
     */
    org.etsi.uri.x01903.v13.CertIDListType addNewCertRefs();

    /**
     * Gets the "Id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "Id" attribute
     */
    org.apache.xmlbeans.XmlID xgetId();

    /**
     * True if has "Id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "Id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "Id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlID id);

    /**
     * Unsets the "Id" attribute
     */
    void unsetId();
}
