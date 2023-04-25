/*
 * XML Type:  OtherCertStatusRefsType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OtherCertStatusRefsType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML OtherCertStatusRefsType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class OtherCertStatusRefsTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.OtherCertStatusRefsType {
    private static final long serialVersionUID = 1L;

    public OtherCertStatusRefsTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "OtherRef"),
    };


    /**
     * Gets a List of "OtherRef" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.AnyType> getOtherRefList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOtherRefArray,
                this::setOtherRefArray,
                this::insertNewOtherRef,
                this::removeOtherRef,
                this::sizeOfOtherRefArray
            );
        }
    }

    /**
     * Gets array of all "OtherRef" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType[] getOtherRefArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.AnyType[0]);
    }

    /**
     * Gets ith "OtherRef" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType getOtherRefArray(int i) {
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
     * Returns number of "OtherRef" element
     */
    @Override
    public int sizeOfOtherRefArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "OtherRef" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOtherRefArray(org.etsi.uri.x01903.v13.AnyType[] otherRefArray) {
        check_orphaned();
        arraySetterHelper(otherRefArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "OtherRef" element
     */
    @Override
    public void setOtherRefArray(int i, org.etsi.uri.x01903.v13.AnyType otherRef) {
        generatedSetterHelperImpl(otherRef, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "OtherRef" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType insertNewOtherRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "OtherRef" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType addNewOtherRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "OtherRef" element
     */
    @Override
    public void removeOtherRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
