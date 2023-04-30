/*
 * XML Type:  CT_SheetData
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SheetData(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTSheetDataImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData {
    private static final long serialVersionUID = 1L;

    public CTSheetDataImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "row"),
    };


    /**
     * Gets a List of "row" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow> getRowList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRowArray,
                this::setRowArray,
                this::insertNewRow,
                this::removeRow,
                this::sizeOfRowArray
            );
        }
    }

    /**
     * Gets array of all "row" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow[] getRowArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow[0]);
    }

    /**
     * Gets ith "row" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow getRowArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "row" element
     */
    @Override
    public int sizeOfRowArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "row" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRowArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow[] rowArray) {
        check_orphaned();
        arraySetterHelper(rowArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "row" element
     */
    @Override
    public void setRowArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow row) {
        generatedSetterHelperImpl(row, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "row" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow insertNewRow(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "row" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow addNewRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "row" element
     */
    @Override
    public void removeRow(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
