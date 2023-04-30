/*
 * XML Type:  ClaimedRolesListType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.ClaimedRolesListType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML ClaimedRolesListType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class ClaimedRolesListTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.ClaimedRolesListType {
    private static final long serialVersionUID = 1L;

    public ClaimedRolesListTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "ClaimedRole"),
    };


    /**
     * Gets a List of "ClaimedRole" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.AnyType> getClaimedRoleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getClaimedRoleArray,
                this::setClaimedRoleArray,
                this::insertNewClaimedRole,
                this::removeClaimedRole,
                this::sizeOfClaimedRoleArray
            );
        }
    }

    /**
     * Gets array of all "ClaimedRole" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType[] getClaimedRoleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.AnyType[0]);
    }

    /**
     * Gets ith "ClaimedRole" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType getClaimedRoleArray(int i) {
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
     * Returns number of "ClaimedRole" element
     */
    @Override
    public int sizeOfClaimedRoleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "ClaimedRole" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setClaimedRoleArray(org.etsi.uri.x01903.v13.AnyType[] claimedRoleArray) {
        check_orphaned();
        arraySetterHelper(claimedRoleArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "ClaimedRole" element
     */
    @Override
    public void setClaimedRoleArray(int i, org.etsi.uri.x01903.v13.AnyType claimedRole) {
        generatedSetterHelperImpl(claimedRole, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ClaimedRole" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType insertNewClaimedRole(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ClaimedRole" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType addNewClaimedRole() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "ClaimedRole" element
     */
    @Override
    public void removeClaimedRole(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
