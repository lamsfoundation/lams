/*
 * XML Type:  SPKIDataType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SPKIDataType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SPKIDataType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class SPKIDataTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SPKIDataType {
    private static final long serialVersionUID = 1L;

    public SPKIDataTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "SPKISexp"),
    };


    /**
     * Gets a List of "SPKISexp" elements
     */
    @Override
    public java.util.List<byte[]> getSPKISexpList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getSPKISexpArray,
                this::setSPKISexpArray,
                this::insertSPKISexp,
                this::removeSPKISexp,
                this::sizeOfSPKISexpArray
            );
        }
    }

    /**
     * Gets array of all "SPKISexp" elements
     */
    @Override
    public byte[][] getSPKISexpArray() {
        return getObjectArray(PROPERTY_QNAME[0], org.apache.xmlbeans.SimpleValue::getByteArrayValue, byte[][]::new);
    }

    /**
     * Gets ith "SPKISexp" element
     */
    @Override
    public byte[] getSPKISexpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) a List of "SPKISexp" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlBase64Binary> xgetSPKISexpList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetSPKISexpArray,
                this::xsetSPKISexpArray,
                this::insertNewSPKISexp,
                this::removeSPKISexp,
                this::sizeOfSPKISexpArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "SPKISexp" elements
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary[] xgetSPKISexpArray() {
        return xgetArray(PROPERTY_QNAME[0], org.apache.xmlbeans.XmlBase64Binary[]::new);
    }

    /**
     * Gets (as xml) ith "SPKISexp" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetSPKISexpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "SPKISexp" element
     */
    @Override
    public int sizeOfSPKISexpArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "SPKISexp" element
     */
    @Override
    public void setSPKISexpArray(byte[][] spkiSexpArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(spkiSexpArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets ith "SPKISexp" element
     */
    @Override
    public void setSPKISexpArray(int i, byte[] spkiSexp) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setByteArrayValue(spkiSexp);
        }
    }

    /**
     * Sets (as xml) array of all "SPKISexp" element
     */
    @Override
    public void xsetSPKISexpArray(org.apache.xmlbeans.XmlBase64Binary[]spkiSexpArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(spkiSexpArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets (as xml) ith "SPKISexp" element
     */
    @Override
    public void xsetSPKISexpArray(int i, org.apache.xmlbeans.XmlBase64Binary spkiSexp) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(spkiSexp);
        }
    }

    /**
     * Inserts the value as the ith "SPKISexp" element
     */
    @Override
    public void insertSPKISexp(int i, byte[] spkiSexp) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            target.setByteArrayValue(spkiSexp);
        }
    }

    /**
     * Appends the value as the last "SPKISexp" element
     */
    @Override
    public void addSPKISexp(byte[] spkiSexp) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            target.setByteArrayValue(spkiSexp);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "SPKISexp" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary insertNewSPKISexp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "SPKISexp" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary addNewSPKISexp() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "SPKISexp" element
     */
    @Override
    public void removeSPKISexp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
