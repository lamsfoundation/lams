/*
 * XML Type:  CT_MR
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTMR
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MR(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public class CTMRImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.math.CTMR {
    private static final long serialVersionUID = 1L;

    public CTMRImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "e"),
    };


    /**
     * Gets a List of "e" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg> getEList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEArray,
                this::setEArray,
                this::insertNewE,
                this::removeE,
                this::sizeOfEArray
            );
        }
    }

    /**
     * Gets array of all "e" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg[] getEArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg[0]);
    }

    /**
     * Gets ith "e" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg getEArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "e" element
     */
    @Override
    public int sizeOfEArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "e" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg[] eArray) {
        check_orphaned();
        arraySetterHelper(eArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "e" element
     */
    @Override
    public void setEArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg e) {
        generatedSetterHelperImpl(e, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "e" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg insertNewE(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "e" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg addNewE() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathArg)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "e" element
     */
    @Override
    public void removeE(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
