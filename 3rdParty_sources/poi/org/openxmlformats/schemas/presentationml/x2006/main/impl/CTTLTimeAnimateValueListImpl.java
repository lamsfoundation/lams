/*
 * XML Type:  CT_TLTimeAnimateValueList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValueList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLTimeAnimateValueList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLTimeAnimateValueListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValueList {
    private static final long serialVersionUID = 1L;

    public CTTLTimeAnimateValueListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "tav"),
    };


    /**
     * Gets a List of "tav" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue> getTavList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTavArray,
                this::setTavArray,
                this::insertNewTav,
                this::removeTav,
                this::sizeOfTavArray
            );
        }
    }

    /**
     * Gets array of all "tav" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue[] getTavArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue[0]);
    }

    /**
     * Gets ith "tav" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue getTavArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tav" element
     */
    @Override
    public int sizeOfTavArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "tav" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTavArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue[] tavArray) {
        check_orphaned();
        arraySetterHelper(tavArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "tav" element
     */
    @Override
    public void setTavArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue tav) {
        generatedSetterHelperImpl(tav, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tav" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue insertNewTav(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tav" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue addNewTav() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "tav" element
     */
    @Override
    public void removeTav(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
