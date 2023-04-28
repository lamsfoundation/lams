/*
 * XML Type:  CertificateValuesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CertificateValuesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CertificateValuesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CertificateValuesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CertificateValuesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "certificatevaluestype5c75type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "EncapsulatedX509Certificate" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.EncapsulatedPKIDataType> getEncapsulatedX509CertificateList();

    /**
     * Gets array of all "EncapsulatedX509Certificate" elements
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] getEncapsulatedX509CertificateArray();

    /**
     * Gets ith "EncapsulatedX509Certificate" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType getEncapsulatedX509CertificateArray(int i);

    /**
     * Returns number of "EncapsulatedX509Certificate" element
     */
    int sizeOfEncapsulatedX509CertificateArray();

    /**
     * Sets array of all "EncapsulatedX509Certificate" element
     */
    void setEncapsulatedX509CertificateArray(org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] encapsulatedX509CertificateArray);

    /**
     * Sets ith "EncapsulatedX509Certificate" element
     */
    void setEncapsulatedX509CertificateArray(int i, org.etsi.uri.x01903.v13.EncapsulatedPKIDataType encapsulatedX509Certificate);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "EncapsulatedX509Certificate" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType insertNewEncapsulatedX509Certificate(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "EncapsulatedX509Certificate" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType addNewEncapsulatedX509Certificate();

    /**
     * Removes the ith "EncapsulatedX509Certificate" element
     */
    void removeEncapsulatedX509Certificate(int i);

    /**
     * Gets a List of "OtherCertificate" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.AnyType> getOtherCertificateList();

    /**
     * Gets array of all "OtherCertificate" elements
     */
    org.etsi.uri.x01903.v13.AnyType[] getOtherCertificateArray();

    /**
     * Gets ith "OtherCertificate" element
     */
    org.etsi.uri.x01903.v13.AnyType getOtherCertificateArray(int i);

    /**
     * Returns number of "OtherCertificate" element
     */
    int sizeOfOtherCertificateArray();

    /**
     * Sets array of all "OtherCertificate" element
     */
    void setOtherCertificateArray(org.etsi.uri.x01903.v13.AnyType[] otherCertificateArray);

    /**
     * Sets ith "OtherCertificate" element
     */
    void setOtherCertificateArray(int i, org.etsi.uri.x01903.v13.AnyType otherCertificate);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "OtherCertificate" element
     */
    org.etsi.uri.x01903.v13.AnyType insertNewOtherCertificate(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "OtherCertificate" element
     */
    org.etsi.uri.x01903.v13.AnyType addNewOtherCertificate();

    /**
     * Removes the ith "OtherCertificate" element
     */
    void removeOtherCertificate(int i);

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
