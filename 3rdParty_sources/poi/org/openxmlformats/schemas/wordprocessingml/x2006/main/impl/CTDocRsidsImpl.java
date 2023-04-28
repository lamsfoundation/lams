/*
 * XML Type:  CT_DocRsids
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocRsids
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DocRsids(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDocRsidsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocRsids {
    private static final long serialVersionUID = 1L;

    public CTDocRsidsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsidRoot"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsid"),
    };


    /**
     * Gets the "rsidRoot" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber getRsidRoot() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rsidRoot" element
     */
    @Override
    public boolean isSetRsidRoot() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "rsidRoot" element
     */
    @Override
    public void setRsidRoot(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber rsidRoot) {
        generatedSetterHelperImpl(rsidRoot, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rsidRoot" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber addNewRsidRoot() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "rsidRoot" element
     */
    @Override
    public void unsetRsidRoot() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets a List of "rsid" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber> getRsidList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRsidArray,
                this::setRsidArray,
                this::insertNewRsid,
                this::removeRsid,
                this::sizeOfRsidArray
            );
        }
    }

    /**
     * Gets array of all "rsid" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber[] getRsidArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber[0]);
    }

    /**
     * Gets ith "rsid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber getRsidArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rsid" element
     */
    @Override
    public int sizeOfRsidArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "rsid" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRsidArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber[] rsidArray) {
        check_orphaned();
        arraySetterHelper(rsidArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "rsid" element
     */
    @Override
    public void setRsidArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber rsid) {
        generatedSetterHelperImpl(rsid, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rsid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber insertNewRsid(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rsid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber addNewRsid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "rsid" element
     */
    @Override
    public void removeRsid(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }
}
