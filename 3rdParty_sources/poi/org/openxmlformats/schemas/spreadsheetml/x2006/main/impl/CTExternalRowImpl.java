/*
 * XML Type:  CT_ExternalRow
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ExternalRow(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTExternalRowImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow {
    private static final long serialVersionUID = 1L;

    public CTExternalRowImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cell"),
        new QName("", "r"),
    };


    /**
     * Gets a List of "cell" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell> getCellList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCellArray,
                this::setCellArray,
                this::insertNewCell,
                this::removeCell,
                this::sizeOfCellArray
            );
        }
    }

    /**
     * Gets array of all "cell" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell[] getCellArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell[0]);
    }

    /**
     * Gets ith "cell" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell getCellArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cell" element
     */
    @Override
    public int sizeOfCellArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cell" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCellArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell[] cellArray) {
        check_orphaned();
        arraySetterHelper(cellArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cell" element
     */
    @Override
    public void setCellArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell cell) {
        generatedSetterHelperImpl(cell, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cell" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell insertNewCell(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cell" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell addNewCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cell" element
     */
    @Override
    public void removeCell(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "r" attribute
     */
    @Override
    public long getR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "r" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "r" attribute
     */
    @Override
    public void setR(long r) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(r);
        }
    }

    /**
     * Sets (as xml) the "r" attribute
     */
    @Override
    public void xsetR(org.apache.xmlbeans.XmlUnsignedInt r) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(r);
        }
    }
}
