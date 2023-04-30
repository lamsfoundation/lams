/*
 * XML Type:  CertifiedRolesListType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CertifiedRolesListType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CertifiedRolesListType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class CertifiedRolesListTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CertifiedRolesListType {
    private static final long serialVersionUID = 1L;

    public CertifiedRolesListTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CertifiedRole"),
    };


    /**
     * Gets a List of "CertifiedRole" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.EncapsulatedPKIDataType> getCertifiedRoleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCertifiedRoleArray,
                this::setCertifiedRoleArray,
                this::insertNewCertifiedRole,
                this::removeCertifiedRole,
                this::sizeOfCertifiedRoleArray
            );
        }
    }

    /**
     * Gets array of all "CertifiedRole" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] getCertifiedRoleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[0]);
    }

    /**
     * Gets ith "CertifiedRole" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType getCertifiedRoleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "CertifiedRole" element
     */
    @Override
    public int sizeOfCertifiedRoleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "CertifiedRole" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCertifiedRoleArray(org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] certifiedRoleArray) {
        check_orphaned();
        arraySetterHelper(certifiedRoleArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "CertifiedRole" element
     */
    @Override
    public void setCertifiedRoleArray(int i, org.etsi.uri.x01903.v13.EncapsulatedPKIDataType certifiedRole) {
        generatedSetterHelperImpl(certifiedRole, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CertifiedRole" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType insertNewCertifiedRole(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "CertifiedRole" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType addNewCertifiedRole() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "CertifiedRole" element
     */
    @Override
    public void removeCertifiedRole(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
