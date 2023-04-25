/*
 * XML Type:  ValidationDataType
 * Namespace: http://uri.etsi.org/01903/v1.4.1#
 * Java type: org.etsi.uri.x01903.v14.ValidationDataType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v14;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ValidationDataType(@http://uri.etsi.org/01903/v1.4.1#).
 *
 * This is a complex type.
 */
public interface ValidationDataType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v14.ValidationDataType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "validationdatatype2c11type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "CertificateValues" element
     */
    org.etsi.uri.x01903.v13.CertificateValuesType getCertificateValues();

    /**
     * True if has "CertificateValues" element
     */
    boolean isSetCertificateValues();

    /**
     * Sets the "CertificateValues" element
     */
    void setCertificateValues(org.etsi.uri.x01903.v13.CertificateValuesType certificateValues);

    /**
     * Appends and returns a new empty "CertificateValues" element
     */
    org.etsi.uri.x01903.v13.CertificateValuesType addNewCertificateValues();

    /**
     * Unsets the "CertificateValues" element
     */
    void unsetCertificateValues();

    /**
     * Gets the "RevocationValues" element
     */
    org.etsi.uri.x01903.v13.RevocationValuesType getRevocationValues();

    /**
     * True if has "RevocationValues" element
     */
    boolean isSetRevocationValues();

    /**
     * Sets the "RevocationValues" element
     */
    void setRevocationValues(org.etsi.uri.x01903.v13.RevocationValuesType revocationValues);

    /**
     * Appends and returns a new empty "RevocationValues" element
     */
    org.etsi.uri.x01903.v13.RevocationValuesType addNewRevocationValues();

    /**
     * Unsets the "RevocationValues" element
     */
    void unsetRevocationValues();

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
