/*
 * XML Type:  CertIDType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CertIDType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CertIDType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CertIDType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CertIDType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "certidtypee64dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "CertDigest" element
     */
    org.etsi.uri.x01903.v13.DigestAlgAndValueType getCertDigest();

    /**
     * Sets the "CertDigest" element
     */
    void setCertDigest(org.etsi.uri.x01903.v13.DigestAlgAndValueType certDigest);

    /**
     * Appends and returns a new empty "CertDigest" element
     */
    org.etsi.uri.x01903.v13.DigestAlgAndValueType addNewCertDigest();

    /**
     * Gets the "IssuerSerial" element
     */
    org.w3.x2000.x09.xmldsig.X509IssuerSerialType getIssuerSerial();

    /**
     * Sets the "IssuerSerial" element
     */
    void setIssuerSerial(org.w3.x2000.x09.xmldsig.X509IssuerSerialType issuerSerial);

    /**
     * Appends and returns a new empty "IssuerSerial" element
     */
    org.w3.x2000.x09.xmldsig.X509IssuerSerialType addNewIssuerSerial();

    /**
     * Gets the "URI" attribute
     */
    java.lang.String getURI();

    /**
     * Gets (as xml) the "URI" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetURI();

    /**
     * True if has "URI" attribute
     */
    boolean isSetURI();

    /**
     * Sets the "URI" attribute
     */
    void setURI(java.lang.String uri);

    /**
     * Sets (as xml) the "URI" attribute
     */
    void xsetURI(org.apache.xmlbeans.XmlAnyURI uri);

    /**
     * Unsets the "URI" attribute
     */
    void unsetURI();
}
