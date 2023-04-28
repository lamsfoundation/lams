/*
 * XML Type:  CommitmentTypeQualifiersListType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CommitmentTypeQualifiersListType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class CommitmentTypeQualifiersListTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType {
    private static final long serialVersionUID = 1L;

    public CommitmentTypeQualifiersListTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CommitmentTypeQualifier"),
    };


    /**
     * Gets a List of "CommitmentTypeQualifier" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.AnyType> getCommitmentTypeQualifierList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCommitmentTypeQualifierArray,
                this::setCommitmentTypeQualifierArray,
                this::insertNewCommitmentTypeQualifier,
                this::removeCommitmentTypeQualifier,
                this::sizeOfCommitmentTypeQualifierArray
            );
        }
    }

    /**
     * Gets array of all "CommitmentTypeQualifier" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType[] getCommitmentTypeQualifierArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.AnyType[0]);
    }

    /**
     * Gets ith "CommitmentTypeQualifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType getCommitmentTypeQualifierArray(int i) {
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
     * Returns number of "CommitmentTypeQualifier" element
     */
    @Override
    public int sizeOfCommitmentTypeQualifierArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "CommitmentTypeQualifier" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCommitmentTypeQualifierArray(org.etsi.uri.x01903.v13.AnyType[] commitmentTypeQualifierArray) {
        check_orphaned();
        arraySetterHelper(commitmentTypeQualifierArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "CommitmentTypeQualifier" element
     */
    @Override
    public void setCommitmentTypeQualifierArray(int i, org.etsi.uri.x01903.v13.AnyType commitmentTypeQualifier) {
        generatedSetterHelperImpl(commitmentTypeQualifier, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CommitmentTypeQualifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType insertNewCommitmentTypeQualifier(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "CommitmentTypeQualifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType addNewCommitmentTypeQualifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "CommitmentTypeQualifier" element
     */
    @Override
    public void removeCommitmentTypeQualifier(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
