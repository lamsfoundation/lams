/*
 * XML Type:  CRLRefsType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CRLRefsType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CRLRefsType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class CRLRefsTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CRLRefsType {
    private static final long serialVersionUID = 1L;

    public CRLRefsTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CRLRef"),
    };


    /**
     * Gets a List of "CRLRef" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.CRLRefType> getCRLRefList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCRLRefArray,
                this::setCRLRefArray,
                this::insertNewCRLRef,
                this::removeCRLRef,
                this::sizeOfCRLRefArray
            );
        }
    }

    /**
     * Gets array of all "CRLRef" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.CRLRefType[] getCRLRefArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.CRLRefType[0]);
    }

    /**
     * Gets ith "CRLRef" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CRLRefType getCRLRefArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CRLRefType target = null;
            target = (org.etsi.uri.x01903.v13.CRLRefType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "CRLRef" element
     */
    @Override
    public int sizeOfCRLRefArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "CRLRef" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCRLRefArray(org.etsi.uri.x01903.v13.CRLRefType[] crlRefArray) {
        check_orphaned();
        arraySetterHelper(crlRefArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "CRLRef" element
     */
    @Override
    public void setCRLRefArray(int i, org.etsi.uri.x01903.v13.CRLRefType crlRef) {
        generatedSetterHelperImpl(crlRef, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CRLRef" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CRLRefType insertNewCRLRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CRLRefType target = null;
            target = (org.etsi.uri.x01903.v13.CRLRefType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "CRLRef" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CRLRefType addNewCRLRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CRLRefType target = null;
            target = (org.etsi.uri.x01903.v13.CRLRefType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "CRLRef" element
     */
    @Override
    public void removeCRLRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
