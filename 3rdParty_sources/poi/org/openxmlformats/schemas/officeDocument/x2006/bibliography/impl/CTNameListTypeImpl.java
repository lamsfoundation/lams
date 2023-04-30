/*
 * XML Type:  CT_NameListType
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_NameListType(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography).
 *
 * This is a complex type.
 */
public class CTNameListTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType {
    private static final long serialVersionUID = 1L;

    public CTNameListTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Person"),
    };


    /**
     * Gets a List of "Person" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType> getPersonList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPersonArray,
                this::setPersonArray,
                this::insertNewPerson,
                this::removePerson,
                this::sizeOfPersonArray
            );
        }
    }

    /**
     * Gets array of all "Person" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType[] getPersonArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType[0]);
    }

    /**
     * Gets ith "Person" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType getPersonArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Person" element
     */
    @Override
    public int sizeOfPersonArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "Person" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPersonArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType[] personArray) {
        check_orphaned();
        arraySetterHelper(personArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "Person" element
     */
    @Override
    public void setPersonArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType person) {
        generatedSetterHelperImpl(person, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Person" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType insertNewPerson(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Person" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType addNewPerson() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "Person" element
     */
    @Override
    public void removePerson(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
