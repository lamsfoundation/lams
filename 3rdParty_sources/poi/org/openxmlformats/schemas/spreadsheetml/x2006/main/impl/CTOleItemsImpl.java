/*
 * XML Type:  CT_OleItems
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItems
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_OleItems(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTOleItemsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItems {
    private static final long serialVersionUID = 1L;

    public CTOleItemsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "oleItem"),
    };


    /**
     * Gets a List of "oleItem" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem> getOleItemList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOleItemArray,
                this::setOleItemArray,
                this::insertNewOleItem,
                this::removeOleItem,
                this::sizeOfOleItemArray
            );
        }
    }

    /**
     * Gets array of all "oleItem" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem[] getOleItemArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem[0]);
    }

    /**
     * Gets ith "oleItem" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem getOleItemArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "oleItem" element
     */
    @Override
    public int sizeOfOleItemArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "oleItem" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOleItemArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem[] oleItemArray) {
        check_orphaned();
        arraySetterHelper(oleItemArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "oleItem" element
     */
    @Override
    public void setOleItemArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem oleItem) {
        generatedSetterHelperImpl(oleItem, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "oleItem" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem insertNewOleItem(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "oleItem" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem addNewOleItem() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "oleItem" element
     */
    @Override
    public void removeOleItem(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
