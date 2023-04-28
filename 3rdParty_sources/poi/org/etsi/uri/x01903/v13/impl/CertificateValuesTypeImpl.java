/*
 * XML Type:  CertificateValuesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CertificateValuesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CertificateValuesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class CertificateValuesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CertificateValuesType {
    private static final long serialVersionUID = 1L;

    public CertificateValuesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "EncapsulatedX509Certificate"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "OtherCertificate"),
        new QName("", "Id"),
    };


    /**
     * Gets a List of "EncapsulatedX509Certificate" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.EncapsulatedPKIDataType> getEncapsulatedX509CertificateList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEncapsulatedX509CertificateArray,
                this::setEncapsulatedX509CertificateArray,
                this::insertNewEncapsulatedX509Certificate,
                this::removeEncapsulatedX509Certificate,
                this::sizeOfEncapsulatedX509CertificateArray
            );
        }
    }

    /**
     * Gets array of all "EncapsulatedX509Certificate" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] getEncapsulatedX509CertificateArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[0]);
    }

    /**
     * Gets ith "EncapsulatedX509Certificate" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType getEncapsulatedX509CertificateArray(int i) {
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
     * Returns number of "EncapsulatedX509Certificate" element
     */
    @Override
    public int sizeOfEncapsulatedX509CertificateArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "EncapsulatedX509Certificate" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEncapsulatedX509CertificateArray(org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] encapsulatedX509CertificateArray) {
        check_orphaned();
        arraySetterHelper(encapsulatedX509CertificateArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "EncapsulatedX509Certificate" element
     */
    @Override
    public void setEncapsulatedX509CertificateArray(int i, org.etsi.uri.x01903.v13.EncapsulatedPKIDataType encapsulatedX509Certificate) {
        generatedSetterHelperImpl(encapsulatedX509Certificate, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "EncapsulatedX509Certificate" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType insertNewEncapsulatedX509Certificate(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "EncapsulatedX509Certificate" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType addNewEncapsulatedX509Certificate() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "EncapsulatedX509Certificate" element
     */
    @Override
    public void removeEncapsulatedX509Certificate(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "OtherCertificate" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.AnyType> getOtherCertificateList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOtherCertificateArray,
                this::setOtherCertificateArray,
                this::insertNewOtherCertificate,
                this::removeOtherCertificate,
                this::sizeOfOtherCertificateArray
            );
        }
    }

    /**
     * Gets array of all "OtherCertificate" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType[] getOtherCertificateArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.etsi.uri.x01903.v13.AnyType[0]);
    }

    /**
     * Gets ith "OtherCertificate" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType getOtherCertificateArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "OtherCertificate" element
     */
    @Override
    public int sizeOfOtherCertificateArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "OtherCertificate" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOtherCertificateArray(org.etsi.uri.x01903.v13.AnyType[] otherCertificateArray) {
        check_orphaned();
        arraySetterHelper(otherCertificateArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "OtherCertificate" element
     */
    @Override
    public void setOtherCertificateArray(int i, org.etsi.uri.x01903.v13.AnyType otherCertificate) {
        generatedSetterHelperImpl(otherCertificate, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "OtherCertificate" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType insertNewOtherCertificate(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "OtherCertificate" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType addNewOtherCertificate() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "OtherCertificate" element
     */
    @Override
    public void removeOtherCertificate(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets the "Id" attribute
     */
    @Override
    public java.lang.String getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Id" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlID xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "Id" attribute
     */
    @Override
    public boolean isSetId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "Id" attribute
     */
    @Override
    public void setId(java.lang.String id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(id);
        }
    }

    /**
     * Sets (as xml) the "Id" attribute
     */
    @Override
    public void xsetId(org.apache.xmlbeans.XmlID id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(id);
        }
    }

    /**
     * Unsets the "Id" attribute
     */
    @Override
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}
