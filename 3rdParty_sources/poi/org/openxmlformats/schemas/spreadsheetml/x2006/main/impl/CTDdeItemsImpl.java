/*
 * XML Type:  CT_DdeItems
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItems
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DdeItems(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTDdeItemsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItems {
    private static final long serialVersionUID = 1L;

    public CTDdeItemsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "ddeItem"),
    };


    /**
     * Gets a List of "ddeItem" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem> getDdeItemList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDdeItemArray,
                this::setDdeItemArray,
                this::insertNewDdeItem,
                this::removeDdeItem,
                this::sizeOfDdeItemArray
            );
        }
    }

    /**
     * Gets array of all "ddeItem" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem[] getDdeItemArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem[0]);
    }

    /**
     * Gets ith "ddeItem" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem getDdeItemArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ddeItem" element
     */
    @Override
    public int sizeOfDdeItemArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "ddeItem" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDdeItemArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem[] ddeItemArray) {
        check_orphaned();
        arraySetterHelper(ddeItemArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "ddeItem" element
     */
    @Override
    public void setDdeItemArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem ddeItem) {
        generatedSetterHelperImpl(ddeItem, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ddeItem" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem insertNewDdeItem(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ddeItem" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem addNewDdeItem() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "ddeItem" element
     */
    @Override
    public void removeDdeItem(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
