/*
 * XML Type:  CertIDListType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CertIDListType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CertIDListType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CertIDListType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CertIDListType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "certidlisttype488btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "Cert" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.CertIDType> getCertList();

    /**
     * Gets array of all "Cert" elements
     */
    org.etsi.uri.x01903.v13.CertIDType[] getCertArray();

    /**
     * Gets ith "Cert" element
     */
    org.etsi.uri.x01903.v13.CertIDType getCertArray(int i);

    /**
     * Returns number of "Cert" element
     */
    int sizeOfCertArray();

    /**
     * Sets array of all "Cert" element
     */
    void setCertArray(org.etsi.uri.x01903.v13.CertIDType[] certArray);

    /**
     * Sets ith "Cert" element
     */
    void setCertArray(int i, org.etsi.uri.x01903.v13.CertIDType cert);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Cert" element
     */
    org.etsi.uri.x01903.v13.CertIDType insertNewCert(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "Cert" element
     */
    org.etsi.uri.x01903.v13.CertIDType addNewCert();

    /**
     * Removes the ith "Cert" element
     */
    void removeCert(int i);
}
