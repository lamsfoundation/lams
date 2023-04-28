/*
 * XML Type:  IntegerListType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.IntegerListType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML IntegerListType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class IntegerListTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.IntegerListType {
    private static final long serialVersionUID = 1L;

    public IntegerListTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "int"),
    };


    /**
     * Gets a List of "int" elements
     */
    @Override
    public java.util.List<java.math.BigInteger> getIntList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getIntArray,
                this::setIntArray,
                this::insertInt,
                this::removeInt,
                this::sizeOfIntArray
            );
        }
    }

    /**
     * Gets array of all "int" elements
     */
    @Override
    public java.math.BigInteger[] getIntArray() {
        return getObjectArray(PROPERTY_QNAME[0], org.apache.xmlbeans.SimpleValue::getBigIntegerValue, java.math.BigInteger[]::new);
    }

    /**
     * Gets ith "int" element
     */
    @Override
    public java.math.BigInteger getIntArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) a List of "int" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlInteger> xgetIntList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetIntArray,
                this::xsetIntArray,
                this::insertNewInt,
                this::removeInt,
                this::sizeOfIntArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "int" elements
     */
    @Override
    public org.apache.xmlbeans.XmlInteger[] xgetIntArray() {
        return xgetArray(PROPERTY_QNAME[0], org.apache.xmlbeans.XmlInteger[]::new);
    }

    /**
     * Gets (as xml) ith "int" element
     */
    @Override
    public org.apache.xmlbeans.XmlInteger xgetIntArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInteger target = null;
            target = (org.apache.xmlbeans.XmlInteger)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "int" element
     */
    @Override
    public int sizeOfIntArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "int" element
     */
    @Override
    public void setIntArray(java.math.BigInteger[] xintArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(xintArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets ith "int" element
     */
    @Override
    public void setIntArray(int i, java.math.BigInteger xint) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setBigIntegerValue(xint);
        }
    }

    /**
     * Sets (as xml) array of all "int" element
     */
    @Override
    public void xsetIntArray(org.apache.xmlbeans.XmlInteger[]xintArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(xintArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets (as xml) ith "int" element
     */
    @Override
    public void xsetIntArray(int i, org.apache.xmlbeans.XmlInteger xint) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInteger target = null;
            target = (org.apache.xmlbeans.XmlInteger)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(xint);
        }
    }

    /**
     * Inserts the value as the ith "int" element
     */
    @Override
    public void insertInt(int i, java.math.BigInteger xint) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            target.setBigIntegerValue(xint);
        }
    }

    /**
     * Appends the value as the last "int" element
     */
    @Override
    public void addInt(java.math.BigInteger xint) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            target.setBigIntegerValue(xint);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "int" element
     */
    @Override
    public org.apache.xmlbeans.XmlInteger insertNewInt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInteger target = null;
            target = (org.apache.xmlbeans.XmlInteger)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "int" element
     */
    @Override
    public org.apache.xmlbeans.XmlInteger addNewInt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInteger target = null;
            target = (org.apache.xmlbeans.XmlInteger)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "int" element
     */
    @Override
    public void removeInt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
