/*
 * XML Type:  CT_ExternalSheetData
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ExternalSheetData(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTExternalSheetDataImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetData {
    private static final long serialVersionUID = 1L;

    public CTExternalSheetDataImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "row"),
        new QName("", "sheetId"),
        new QName("", "refreshError"),
    };


    /**
     * Gets a List of "row" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow> getRowList() {
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
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow[] getRowArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow[0]);
    }

    /**
     * Gets ith "row" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow getRowArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow)get_store().find_element_user(PROPERTY_QNAME[0], i);
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
    public void setRowArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow[] rowArray) {
        check_orphaned();
        arraySetterHelper(rowArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "row" element
     */
    @Override
    public void setRowArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow row) {
        generatedSetterHelperImpl(row, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "row" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow insertNewRow(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "row" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow addNewRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow)get_store().add_element_user(PROPERTY_QNAME[0]);
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

    /**
     * Gets the "sheetId" attribute
     */
    @Override
    public long getSheetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "sheetId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetSheetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "sheetId" attribute
     */
    @Override
    public void setSheetId(long sheetId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(sheetId);
        }
    }

    /**
     * Sets (as xml) the "sheetId" attribute
     */
    @Override
    public void xsetSheetId(org.apache.xmlbeans.XmlUnsignedInt sheetId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(sheetId);
        }
    }

    /**
     * Gets the "refreshError" attribute
     */
    @Override
    public boolean getRefreshError() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "refreshError" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetRefreshError() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "refreshError" attribute
     */
    @Override
    public boolean isSetRefreshError() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "refreshError" attribute
     */
    @Override
    public void setRefreshError(boolean refreshError) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBooleanValue(refreshError);
        }
    }

    /**
     * Sets (as xml) the "refreshError" attribute
     */
    @Override
    public void xsetRefreshError(org.apache.xmlbeans.XmlBoolean refreshError) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(refreshError);
        }
    }

    /**
     * Unsets the "refreshError" attribute
     */
    @Override
    public void unsetRefreshError() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}
