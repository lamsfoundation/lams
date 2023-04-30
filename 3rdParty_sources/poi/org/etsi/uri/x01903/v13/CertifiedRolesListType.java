/*
 * XML Type:  CertifiedRolesListType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CertifiedRolesListType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CertifiedRolesListType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CertifiedRolesListType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CertifiedRolesListType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "certifiedroleslisttype2aa2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "CertifiedRole" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.EncapsulatedPKIDataType> getCertifiedRoleList();

    /**
     * Gets array of all "CertifiedRole" elements
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] getCertifiedRoleArray();

    /**
     * Gets ith "CertifiedRole" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType getCertifiedRoleArray(int i);

    /**
     * Returns number of "CertifiedRole" element
     */
    int sizeOfCertifiedRoleArray();

    /**
     * Sets array of all "CertifiedRole" element
     */
    void setCertifiedRoleArray(org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] certifiedRoleArray);

    /**
     * Sets ith "CertifiedRole" element
     */
    void setCertifiedRoleArray(int i, org.etsi.uri.x01903.v13.EncapsulatedPKIDataType certifiedRole);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CertifiedRole" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType insertNewCertifiedRole(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "CertifiedRole" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType addNewCertifiedRole();

    /**
     * Removes the ith "CertifiedRole" element
     */
    void removeCertifiedRole(int i);
}
