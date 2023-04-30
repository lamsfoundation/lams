/*
 * XML Type:  OCSPRefsType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OCSPRefsType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML OCSPRefsType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class OCSPRefsTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.OCSPRefsType {
    private static final long serialVersionUID = 1L;

    public OCSPRefsTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "OCSPRef"),
    };


    /**
     * Gets a List of "OCSPRef" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.OCSPRefType> getOCSPRefList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOCSPRefArray,
                this::setOCSPRefArray,
                this::insertNewOCSPRef,
                this::removeOCSPRef,
                this::sizeOfOCSPRefArray
            );
        }
    }

    /**
     * Gets array of all "OCSPRef" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.OCSPRefType[] getOCSPRefArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.OCSPRefType[0]);
    }

    /**
     * Gets ith "OCSPRef" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OCSPRefType getOCSPRefArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OCSPRefType target = null;
            target = (org.etsi.uri.x01903.v13.OCSPRefType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "OCSPRef" element
     */
    @Override
    public int sizeOfOCSPRefArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "OCSPRef" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOCSPRefArray(org.etsi.uri.x01903.v13.OCSPRefType[] ocspRefArray) {
        check_orphaned();
        arraySetterHelper(ocspRefArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "OCSPRef" element
     */
    @Override
    public void setOCSPRefArray(int i, org.etsi.uri.x01903.v13.OCSPRefType ocspRef) {
        generatedSetterHelperImpl(ocspRef, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "OCSPRef" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OCSPRefType insertNewOCSPRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OCSPRefType target = null;
            target = (org.etsi.uri.x01903.v13.OCSPRefType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "OCSPRef" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OCSPRefType addNewOCSPRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OCSPRefType target = null;
            target = (org.etsi.uri.x01903.v13.OCSPRefType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "OCSPRef" element
     */
    @Override
    public void removeOCSPRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
