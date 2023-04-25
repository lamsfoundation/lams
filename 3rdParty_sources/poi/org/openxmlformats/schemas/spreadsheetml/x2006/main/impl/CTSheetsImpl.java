/*
 * XML Type:  CT_Sheets
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Sheets(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTSheetsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets {
    private static final long serialVersionUID = 1L;

    public CTSheetsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheet"),
    };


    /**
     * Gets a List of "sheet" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet> getSheetList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSheetArray,
                this::setSheetArray,
                this::insertNewSheet,
                this::removeSheet,
                this::sizeOfSheetArray
            );
        }
    }

    /**
     * Gets array of all "sheet" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet[] getSheetArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet[0]);
    }

    /**
     * Gets ith "sheet" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet getSheetArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sheet" element
     */
    @Override
    public int sizeOfSheetArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "sheet" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSheetArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet[] sheetArray) {
        check_orphaned();
        arraySetterHelper(sheetArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "sheet" element
     */
    @Override
    public void setSheetArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet sheet) {
        generatedSetterHelperImpl(sheet, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sheet" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet insertNewSheet(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sheet" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet addNewSheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "sheet" element
     */
    @Override
    public void removeSheet(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
