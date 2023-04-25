/*
 * XML Type:  CT_ProtectedRanges
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRanges
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ProtectedRanges(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTProtectedRangesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRanges {
    private static final long serialVersionUID = 1L;

    public CTProtectedRangesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "protectedRange"),
    };


    /**
     * Gets a List of "protectedRange" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange> getProtectedRangeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getProtectedRangeArray,
                this::setProtectedRangeArray,
                this::insertNewProtectedRange,
                this::removeProtectedRange,
                this::sizeOfProtectedRangeArray
            );
        }
    }

    /**
     * Gets array of all "protectedRange" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange[] getProtectedRangeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange[0]);
    }

    /**
     * Gets ith "protectedRange" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange getProtectedRangeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "protectedRange" element
     */
    @Override
    public int sizeOfProtectedRangeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "protectedRange" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setProtectedRangeArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange[] protectedRangeArray) {
        check_orphaned();
        arraySetterHelper(protectedRangeArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "protectedRange" element
     */
    @Override
    public void setProtectedRangeArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange protectedRange) {
        generatedSetterHelperImpl(protectedRange, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "protectedRange" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange insertNewProtectedRange(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "protectedRange" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange addNewProtectedRange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "protectedRange" element
     */
    @Override
    public void removeProtectedRange(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
