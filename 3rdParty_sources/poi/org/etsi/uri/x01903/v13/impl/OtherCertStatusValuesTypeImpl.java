/*
 * XML Type:  OtherCertStatusValuesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OtherCertStatusValuesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML OtherCertStatusValuesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class OtherCertStatusValuesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.OtherCertStatusValuesType {
    private static final long serialVersionUID = 1L;

    public OtherCertStatusValuesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "OtherValue"),
    };


    /**
     * Gets a List of "OtherValue" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.AnyType> getOtherValueList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOtherValueArray,
                this::setOtherValueArray,
                this::insertNewOtherValue,
                this::removeOtherValue,
                this::sizeOfOtherValueArray
            );
        }
    }

    /**
     * Gets array of all "OtherValue" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType[] getOtherValueArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.AnyType[0]);
    }

    /**
     * Gets ith "OtherValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType getOtherValueArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "OtherValue" element
     */
    @Override
    public int sizeOfOtherValueArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "OtherValue" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOtherValueArray(org.etsi.uri.x01903.v13.AnyType[] otherValueArray) {
        check_orphaned();
        arraySetterHelper(otherValueArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "OtherValue" element
     */
    @Override
    public void setOtherValueArray(int i, org.etsi.uri.x01903.v13.AnyType otherValue) {
        generatedSetterHelperImpl(otherValue, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "OtherValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType insertNewOtherValue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "OtherValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType addNewOtherValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "OtherValue" element
     */
    @Override
    public void removeOtherValue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
