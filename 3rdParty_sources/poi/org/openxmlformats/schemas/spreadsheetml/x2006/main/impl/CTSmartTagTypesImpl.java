/*
 * XML Type:  CT_SmartTagTypes
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagTypes
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SmartTagTypes(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTSmartTagTypesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagTypes {
    private static final long serialVersionUID = 1L;

    public CTSmartTagTypesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "smartTagType"),
    };


    /**
     * Gets a List of "smartTagType" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType> getSmartTagTypeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSmartTagTypeArray,
                this::setSmartTagTypeArray,
                this::insertNewSmartTagType,
                this::removeSmartTagType,
                this::sizeOfSmartTagTypeArray
            );
        }
    }

    /**
     * Gets array of all "smartTagType" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType[] getSmartTagTypeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType[0]);
    }

    /**
     * Gets ith "smartTagType" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType getSmartTagTypeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "smartTagType" element
     */
    @Override
    public int sizeOfSmartTagTypeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "smartTagType" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSmartTagTypeArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType[] smartTagTypeArray) {
        check_orphaned();
        arraySetterHelper(smartTagTypeArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "smartTagType" element
     */
    @Override
    public void setSmartTagTypeArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType smartTagType) {
        generatedSetterHelperImpl(smartTagType, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "smartTagType" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType insertNewSmartTagType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "smartTagType" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType addNewSmartTagType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "smartTagType" element
     */
    @Override
    public void removeSmartTagType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
