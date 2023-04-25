/*
 * XML Type:  CT_PersonType
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PersonType(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography).
 *
 * This is a complex type.
 */
public class CTPersonTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTPersonType {
    private static final long serialVersionUID = 1L;

    public CTPersonTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Last"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "First"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Middle"),
    };


    /**
     * Gets a List of "Last" elements
     */
    @Override
    public java.util.List<java.lang.String> getLastList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getLastArray,
                this::setLastArray,
                this::insertLast,
                this::removeLast,
                this::sizeOfLastArray
            );
        }
    }

    /**
     * Gets array of all "Last" elements
     */
    @Override
    public java.lang.String[] getLastArray() {
        return getObjectArray(PROPERTY_QNAME[0], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Last" element
     */
    @Override
    public java.lang.String getLastArray(int i) {
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
     * Gets (as xml) a List of "Last" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetLastList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetLastArray,
                this::xsetLastArray,
                this::insertNewLast,
                this::removeLast,
                this::sizeOfLastArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Last" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetLastArray() {
        return xgetArray(PROPERTY_QNAME[0], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Last" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetLastArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Last" element
     */
    @Override
    public int sizeOfLastArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "Last" element
     */
    @Override
    public void setLastArray(java.lang.String[] lastArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(lastArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets ith "Last" element
     */
    @Override
    public void setLastArray(int i, java.lang.String last) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(last);
        }
    }

    /**
     * Sets (as xml) array of all "Last" element
     */
    @Override
    public void xsetLastArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]lastArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(lastArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets (as xml) ith "Last" element
     */
    @Override
    public void xsetLastArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString last) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(last);
        }
    }

    /**
     * Inserts the value as the ith "Last" element
     */
    @Override
    public void insertLast(int i, java.lang.String last) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            target.setStringValue(last);
        }
    }

    /**
     * Appends the value as the last "Last" element
     */
    @Override
    public void addLast(java.lang.String last) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            target.setStringValue(last);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Last" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewLast(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Last" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewLast() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "Last" element
     */
    @Override
    public void removeLast(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "First" elements
     */
    @Override
    public java.util.List<java.lang.String> getFirstList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getFirstArray,
                this::setFirstArray,
                this::insertFirst,
                this::removeFirst,
                this::sizeOfFirstArray
            );
        }
    }

    /**
     * Gets array of all "First" elements
     */
    @Override
    public java.lang.String[] getFirstArray() {
        return getObjectArray(PROPERTY_QNAME[1], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "First" element
     */
    @Override
    public java.lang.String getFirstArray(int i) {
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
     * Gets (as xml) a List of "First" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetFirstList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetFirstArray,
                this::xsetFirstArray,
                this::insertNewFirst,
                this::removeFirst,
                this::sizeOfFirstArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "First" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetFirstArray() {
        return xgetArray(PROPERTY_QNAME[1], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "First" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetFirstArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "First" element
     */
    @Override
    public int sizeOfFirstArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "First" element
     */
    @Override
    public void setFirstArray(java.lang.String[] firstArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(firstArray, PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets ith "First" element
     */
    @Override
    public void setFirstArray(int i, java.lang.String first) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(first);
        }
    }

    /**
     * Sets (as xml) array of all "First" element
     */
    @Override
    public void xsetFirstArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]firstArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(firstArray, PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets (as xml) ith "First" element
     */
    @Override
    public void xsetFirstArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString first) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(first);
        }
    }

    /**
     * Inserts the value as the ith "First" element
     */
    @Override
    public void insertFirst(int i, java.lang.String first) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            target.setStringValue(first);
        }
    }

    /**
     * Appends the value as the last "First" element
     */
    @Override
    public void addFirst(java.lang.String first) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            target.setStringValue(first);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "First" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewFirst(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "First" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewFirst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "First" element
     */
    @Override
    public void removeFirst(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "Middle" elements
     */
    @Override
    public java.util.List<java.lang.String> getMiddleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getMiddleArray,
                this::setMiddleArray,
                this::insertMiddle,
                this::removeMiddle,
                this::sizeOfMiddleArray
            );
        }
    }

    /**
     * Gets array of all "Middle" elements
     */
    @Override
    public java.lang.String[] getMiddleArray() {
        return getObjectArray(PROPERTY_QNAME[2], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Middle" element
     */
    @Override
    public java.lang.String getMiddleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Middle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetMiddleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetMiddleArray,
                this::xsetMiddleArray,
                this::insertNewMiddle,
                this::removeMiddle,
                this::sizeOfMiddleArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Middle" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetMiddleArray() {
        return xgetArray(PROPERTY_QNAME[2], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Middle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetMiddleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Middle" element
     */
    @Override
    public int sizeOfMiddleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "Middle" element
     */
    @Override
    public void setMiddleArray(java.lang.String[] middleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(middleArray, PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets ith "Middle" element
     */
    @Override
    public void setMiddleArray(int i, java.lang.String middle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(middle);
        }
    }

    /**
     * Sets (as xml) array of all "Middle" element
     */
    @Override
    public void xsetMiddleArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]middleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(middleArray, PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets (as xml) ith "Middle" element
     */
    @Override
    public void xsetMiddleArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString middle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(middle);
        }
    }

    /**
     * Inserts the value as the ith "Middle" element
     */
    @Override
    public void insertMiddle(int i, java.lang.String middle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            target.setStringValue(middle);
        }
    }

    /**
     * Appends the value as the last "Middle" element
     */
    @Override
    public void addMiddle(java.lang.String middle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[2]);
            target.setStringValue(middle);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Middle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewMiddle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Middle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewMiddle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "Middle" element
     */
    @Override
    public void removeMiddle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }
}
