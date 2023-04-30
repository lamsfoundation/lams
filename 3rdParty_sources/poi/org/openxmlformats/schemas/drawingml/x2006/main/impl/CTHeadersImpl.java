/*
 * XML Type:  CT_Headers
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTHeaders
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Headers(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTHeadersImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTHeaders {
    private static final long serialVersionUID = 1L;

    public CTHeadersImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "header"),
    };


    /**
     * Gets a List of "header" elements
     */
    @Override
    public java.util.List<java.lang.String> getHeaderList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getHeaderArray,
                this::setHeaderArray,
                this::insertHeader,
                this::removeHeader,
                this::sizeOfHeaderArray
            );
        }
    }

    /**
     * Gets array of all "header" elements
     */
    @Override
    public java.lang.String[] getHeaderArray() {
        return getObjectArray(PROPERTY_QNAME[0], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "header" element
     */
    @Override
    public java.lang.String getHeaderArray(int i) {
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
     * Gets (as xml) a List of "header" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlString> xgetHeaderList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetHeaderArray,
                this::xsetHeaderArray,
                this::insertNewHeader,
                this::removeHeader,
                this::sizeOfHeaderArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "header" elements
     */
    @Override
    public org.apache.xmlbeans.XmlString[] xgetHeaderArray() {
        return xgetArray(PROPERTY_QNAME[0], org.apache.xmlbeans.XmlString[]::new);
    }

    /**
     * Gets (as xml) ith "header" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetHeaderArray(int i) {
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
     * Returns number of "header" element
     */
    @Override
    public int sizeOfHeaderArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "header" element
     */
    @Override
    public void setHeaderArray(java.lang.String[] headerArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(headerArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets ith "header" element
     */
    @Override
    public void setHeaderArray(int i, java.lang.String header) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(header);
        }
    }

    /**
     * Sets (as xml) array of all "header" element
     */
    @Override
    public void xsetHeaderArray(org.apache.xmlbeans.XmlString[]headerArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(headerArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets (as xml) ith "header" element
     */
    @Override
    public void xsetHeaderArray(int i, org.apache.xmlbeans.XmlString header) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(header);
        }
    }

    /**
     * Inserts the value as the ith "header" element
     */
    @Override
    public void insertHeader(int i, java.lang.String header) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            target.setStringValue(header);
        }
    }

    /**
     * Appends the value as the last "header" element
     */
    @Override
    public void addHeader(java.lang.String header) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            target.setStringValue(header);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "header" element
     */
    @Override
    public org.apache.xmlbeans.XmlString insertNewHeader(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "header" element
     */
    @Override
    public org.apache.xmlbeans.XmlString addNewHeader() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "header" element
     */
    @Override
    public void removeHeader(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
