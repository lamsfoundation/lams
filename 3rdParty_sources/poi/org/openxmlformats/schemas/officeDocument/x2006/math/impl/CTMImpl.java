/*
 * XML Type:  CT_M
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTM
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_M(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public class CTMImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.math.CTM {
    private static final long serialVersionUID = 1L;

    public CTMImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "mPr"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "mr"),
    };


    /**
     * Gets the "mPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMPr getMPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMPr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMPr)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mPr" element
     */
    @Override
    public boolean isSetMPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "mPr" element
     */
    @Override
    public void setMPr(org.openxmlformats.schemas.officeDocument.x2006.math.CTMPr mPr) {
        generatedSetterHelperImpl(mPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMPr addNewMPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMPr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMPr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "mPr" element
     */
    @Override
    public void unsetMPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets a List of "mr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTMR> getMrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMrArray,
                this::setMrArray,
                this::insertNewMr,
                this::removeMr,
                this::sizeOfMrArray
            );
        }
    }

    /**
     * Gets array of all "mr" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMR[] getMrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.officeDocument.x2006.math.CTMR[0]);
    }

    /**
     * Gets ith "mr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMR getMrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMR target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMR)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "mr" element
     */
    @Override
    public int sizeOfMrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "mr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMrArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTMR[] mrArray) {
        check_orphaned();
        arraySetterHelper(mrArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "mr" element
     */
    @Override
    public void setMrArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTMR mr) {
        generatedSetterHelperImpl(mr, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "mr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMR insertNewMr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMR target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMR)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "mr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMR addNewMr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMR target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMR)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "mr" element
     */
    @Override
    public void removeMr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }
}
