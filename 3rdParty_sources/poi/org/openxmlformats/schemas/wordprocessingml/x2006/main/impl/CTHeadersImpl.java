/*
 * XML Type:  CT_Headers
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeaders
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Headers(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTHeadersImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeaders {
    private static final long serialVersionUID = 1L;

    public CTHeadersImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "header"),
    };


    /**
     * Gets a List of "header" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString> getHeaderList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getHeaderArray,
                this::setHeaderArray,
                this::insertNewHeader,
                this::removeHeader,
                this::sizeOfHeaderArray
            );
        }
    }

    /**
     * Gets array of all "header" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[] getHeaderArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[0]);
    }

    /**
     * Gets ith "header" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getHeaderArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[0], i);
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
     * Sets array of all "header" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setHeaderArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[] headerArray) {
        check_orphaned();
        arraySetterHelper(headerArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "header" element
     */
    @Override
    public void setHeaderArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString header) {
        generatedSetterHelperImpl(header, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "header" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString insertNewHeader(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "header" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewHeader() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[0]);
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
