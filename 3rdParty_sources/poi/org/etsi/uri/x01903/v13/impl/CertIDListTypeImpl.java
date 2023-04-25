/*
 * XML Type:  CertIDListType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CertIDListType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CertIDListType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class CertIDListTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CertIDListType {
    private static final long serialVersionUID = 1L;

    public CertIDListTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "Cert"),
    };


    /**
     * Gets a List of "Cert" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.CertIDType> getCertList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCertArray,
                this::setCertArray,
                this::insertNewCert,
                this::removeCert,
                this::sizeOfCertArray
            );
        }
    }

    /**
     * Gets array of all "Cert" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.CertIDType[] getCertArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.CertIDType[0]);
    }

    /**
     * Gets ith "Cert" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertIDType getCertArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertIDType target = null;
            target = (org.etsi.uri.x01903.v13.CertIDType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Cert" element
     */
    @Override
    public int sizeOfCertArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "Cert" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCertArray(org.etsi.uri.x01903.v13.CertIDType[] certArray) {
        check_orphaned();
        arraySetterHelper(certArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "Cert" element
     */
    @Override
    public void setCertArray(int i, org.etsi.uri.x01903.v13.CertIDType cert) {
        generatedSetterHelperImpl(cert, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Cert" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertIDType insertNewCert(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertIDType target = null;
            target = (org.etsi.uri.x01903.v13.CertIDType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Cert" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertIDType addNewCert() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertIDType target = null;
            target = (org.etsi.uri.x01903.v13.CertIDType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "Cert" element
     */
    @Override
    public void removeCert(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
