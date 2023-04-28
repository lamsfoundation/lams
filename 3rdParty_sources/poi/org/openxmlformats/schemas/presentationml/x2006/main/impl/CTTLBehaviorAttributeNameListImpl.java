/*
 * XML Type:  CT_TLBehaviorAttributeNameList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLBehaviorAttributeNameList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLBehaviorAttributeNameList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLBehaviorAttributeNameListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLBehaviorAttributeNameList {
    private static final long serialVersionUID = 1L;

    public CTTLBehaviorAttributeNameListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "attrName"),
    };


    /**
     * Gets a List of "attrName" elements
     */
    @Override
    public java.util.List<java.lang.String> getAttrNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getAttrNameArray,
                this::setAttrNameArray,
                this::insertAttrName,
                this::removeAttrName,
                this::sizeOfAttrNameArray
            );
        }
    }

    /**
     * Gets array of all "attrName" elements
     */
    @Override
    public java.lang.String[] getAttrNameArray() {
        return getObjectArray(PROPERTY_QNAME[0], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "attrName" element
     */
    @Override
    public java.lang.String getAttrNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "attrName" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlString> xgetAttrNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetAttrNameArray,
                this::xsetAttrNameArray,
                this::insertNewAttrName,
                this::removeAttrName,
                this::sizeOfAttrNameArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "attrName" elements
     */
    @Override
    public org.apache.xmlbeans.XmlString[] xgetAttrNameArray() {
        return xgetArray(PROPERTY_QNAME[0], org.apache.xmlbeans.XmlString[]::new);
    }

    /**
     * Gets (as xml) ith "attrName" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetAttrNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "attrName" element
     */
    @Override
    public int sizeOfAttrNameArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "attrName" element
     */
    @Override
    public void setAttrNameArray(java.lang.String[] attrNameArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(attrNameArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets ith "attrName" element
     */
    @Override
    public void setAttrNameArray(int i, java.lang.String attrName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(attrName);
        }
    }

    /**
     * Sets (as xml) array of all "attrName" element
     */
    @Override
    public void xsetAttrNameArray(org.apache.xmlbeans.XmlString[]attrNameArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(attrNameArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets (as xml) ith "attrName" element
     */
    @Override
    public void xsetAttrNameArray(int i, org.apache.xmlbeans.XmlString attrName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(attrName);
        }
    }

    /**
     * Inserts the value as the ith "attrName" element
     */
    @Override
    public void insertAttrName(int i, java.lang.String attrName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            target.setStringValue(attrName);
        }
    }

    /**
     * Appends the value as the last "attrName" element
     */
    @Override
    public void addAttrName(java.lang.String attrName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            target.setStringValue(attrName);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "attrName" element
     */
    @Override
    public org.apache.xmlbeans.XmlString insertNewAttrName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "attrName" element
     */
    @Override
    public org.apache.xmlbeans.XmlString addNewAttrName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "attrName" element
     */
    @Override
    public void removeAttrName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
