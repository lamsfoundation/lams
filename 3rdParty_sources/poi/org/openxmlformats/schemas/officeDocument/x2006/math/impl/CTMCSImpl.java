/*
 * XML Type:  CT_MCS
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTMCS
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MCS(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public class CTMCSImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.math.CTMCS {
    private static final long serialVersionUID = 1L;

    public CTMCSImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "mc"),
    };


    /**
     * Gets a List of "mc" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTMC> getMcList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMcArray,
                this::setMcArray,
                this::insertNewMc,
                this::removeMc,
                this::sizeOfMcArray
            );
        }
    }

    /**
     * Gets array of all "mc" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMC[] getMcArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.officeDocument.x2006.math.CTMC[0]);
    }

    /**
     * Gets ith "mc" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMC getMcArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMC target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMC)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "mc" element
     */
    @Override
    public int sizeOfMcArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "mc" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMcArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTMC[] mcArray) {
        check_orphaned();
        arraySetterHelper(mcArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "mc" element
     */
    @Override
    public void setMcArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTMC mc) {
        generatedSetterHelperImpl(mc, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "mc" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMC insertNewMc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMC target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMC)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "mc" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMC addNewMc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMC target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMC)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "mc" element
     */
    @Override
    public void removeMc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
