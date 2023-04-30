/*
 * XML Type:  SigPolicyQualifiersListType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SigPolicyQualifiersListType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SigPolicyQualifiersListType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class SigPolicyQualifiersListTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SigPolicyQualifiersListType {
    private static final long serialVersionUID = 1L;

    public SigPolicyQualifiersListTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SigPolicyQualifier"),
    };


    /**
     * Gets a List of "SigPolicyQualifier" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.AnyType> getSigPolicyQualifierList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSigPolicyQualifierArray,
                this::setSigPolicyQualifierArray,
                this::insertNewSigPolicyQualifier,
                this::removeSigPolicyQualifier,
                this::sizeOfSigPolicyQualifierArray
            );
        }
    }

    /**
     * Gets array of all "SigPolicyQualifier" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType[] getSigPolicyQualifierArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.AnyType[0]);
    }

    /**
     * Gets ith "SigPolicyQualifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType getSigPolicyQualifierArray(int i) {
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
     * Returns number of "SigPolicyQualifier" element
     */
    @Override
    public int sizeOfSigPolicyQualifierArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "SigPolicyQualifier" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSigPolicyQualifierArray(org.etsi.uri.x01903.v13.AnyType[] sigPolicyQualifierArray) {
        check_orphaned();
        arraySetterHelper(sigPolicyQualifierArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "SigPolicyQualifier" element
     */
    @Override
    public void setSigPolicyQualifierArray(int i, org.etsi.uri.x01903.v13.AnyType sigPolicyQualifier) {
        generatedSetterHelperImpl(sigPolicyQualifier, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "SigPolicyQualifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType insertNewSigPolicyQualifier(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "SigPolicyQualifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType addNewSigPolicyQualifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "SigPolicyQualifier" element
     */
    @Override
    public void removeSigPolicyQualifier(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
