/*
 * XML Type:  CT_Cols
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Cols(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTColsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols {
    private static final long serialVersionUID = 1L;

    public CTColsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "col"),
    };


    /**
     * Gets a List of "col" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol> getColList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getColArray,
                this::setColArray,
                this::insertNewCol,
                this::removeCol,
                this::sizeOfColArray
            );
        }
    }

    /**
     * Gets array of all "col" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol[] getColArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol[0]);
    }

    /**
     * Gets ith "col" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol getColArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "col" element
     */
    @Override
    public int sizeOfColArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "col" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setColArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol[] colArray) {
        check_orphaned();
        arraySetterHelper(colArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "col" element
     */
    @Override
    public void setColArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol col) {
        generatedSetterHelperImpl(col, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "col" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol insertNewCol(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "col" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol addNewCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "col" element
     */
    @Override
    public void removeCol(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
