/*
 * XML Type:  CommitmentTypeIndicationType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CommitmentTypeIndicationType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CommitmentTypeIndicationType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class CommitmentTypeIndicationTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CommitmentTypeIndicationType {
    private static final long serialVersionUID = 1L;

    public CommitmentTypeIndicationTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CommitmentTypeId"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "ObjectReference"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "AllSignedDataObjects"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CommitmentTypeQualifiers"),
    };


    /**
     * Gets the "CommitmentTypeId" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ObjectIdentifierType getCommitmentTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ObjectIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.ObjectIdentifierType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "CommitmentTypeId" element
     */
    @Override
    public void setCommitmentTypeId(org.etsi.uri.x01903.v13.ObjectIdentifierType commitmentTypeId) {
        generatedSetterHelperImpl(commitmentTypeId, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CommitmentTypeId" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ObjectIdentifierType addNewCommitmentTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ObjectIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.ObjectIdentifierType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets a List of "ObjectReference" elements
     */
    @Override
    public java.util.List<java.lang.String> getObjectReferenceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getObjectReferenceArray,
                this::setObjectReferenceArray,
                this::insertObjectReference,
                this::removeObjectReference,
                this::sizeOfObjectReferenceArray
            );
        }
    }

    /**
     * Gets array of all "ObjectReference" elements
     */
    @Override
    public java.lang.String[] getObjectReferenceArray() {
        return getObjectArray(PROPERTY_QNAME[1], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "ObjectReference" element
     */
    @Override
    public java.lang.String getObjectReferenceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "ObjectReference" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlAnyURI> xgetObjectReferenceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetObjectReferenceArray,
                this::xsetObjectReferenceArray,
                this::insertNewObjectReference,
                this::removeObjectReference,
                this::sizeOfObjectReferenceArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "ObjectReference" elements
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI[] xgetObjectReferenceArray() {
        return xgetArray(PROPERTY_QNAME[1], org.apache.xmlbeans.XmlAnyURI[]::new);
    }

    /**
     * Gets (as xml) ith "ObjectReference" element
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI xgetObjectReferenceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ObjectReference" element
     */
    @Override
    public int sizeOfObjectReferenceArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "ObjectReference" element
     */
    @Override
    public void setObjectReferenceArray(java.lang.String[] objectReferenceArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(objectReferenceArray, PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets ith "ObjectReference" element
     */
    @Override
    public void setObjectReferenceArray(int i, java.lang.String objectReference) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(objectReference);
        }
    }

    /**
     * Sets (as xml) array of all "ObjectReference" element
     */
    @Override
    public void xsetObjectReferenceArray(org.apache.xmlbeans.XmlAnyURI[]objectReferenceArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(objectReferenceArray, PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets (as xml) ith "ObjectReference" element
     */
    @Override
    public void xsetObjectReferenceArray(int i, org.apache.xmlbeans.XmlAnyURI objectReference) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(objectReference);
        }
    }

    /**
     * Inserts the value as the ith "ObjectReference" element
     */
    @Override
    public void insertObjectReference(int i, java.lang.String objectReference) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            target.setStringValue(objectReference);
        }
    }

    /**
     * Appends the value as the last "ObjectReference" element
     */
    @Override
    public void addObjectReference(java.lang.String objectReference) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            target.setStringValue(objectReference);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ObjectReference" element
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI insertNewObjectReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ObjectReference" element
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI addNewObjectReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "ObjectReference" element
     */
    @Override
    public void removeObjectReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets the "AllSignedDataObjects" element
     */
    @Override
    public org.apache.xmlbeans.XmlObject getAllSignedDataObjects() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "AllSignedDataObjects" element
     */
    @Override
    public boolean isSetAllSignedDataObjects() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "AllSignedDataObjects" element
     */
    @Override
    public void setAllSignedDataObjects(org.apache.xmlbeans.XmlObject allSignedDataObjects) {
        generatedSetterHelperImpl(allSignedDataObjects, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "AllSignedDataObjects" element
     */
    @Override
    public org.apache.xmlbeans.XmlObject addNewAllSignedDataObjects() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "AllSignedDataObjects" element
     */
    @Override
    public void unsetAllSignedDataObjects() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "CommitmentTypeQualifiers" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType getCommitmentTypeQualifiers() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType target = null;
            target = (org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "CommitmentTypeQualifiers" element
     */
    @Override
    public boolean isSetCommitmentTypeQualifiers() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "CommitmentTypeQualifiers" element
     */
    @Override
    public void setCommitmentTypeQualifiers(org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType commitmentTypeQualifiers) {
        generatedSetterHelperImpl(commitmentTypeQualifiers, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CommitmentTypeQualifiers" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType addNewCommitmentTypeQualifiers() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType target = null;
            target = (org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "CommitmentTypeQualifiers" element
     */
    @Override
    public void unsetCommitmentTypeQualifiers() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}
