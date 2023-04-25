/*
 * XML Type:  CT_SingleXmlCells
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SingleXmlCells(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTSingleXmlCellsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells {
    private static final long serialVersionUID = 1L;

    public CTSingleXmlCellsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "singleXmlCell"),
    };


    /**
     * Gets a List of "singleXmlCell" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell> getSingleXmlCellList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSingleXmlCellArray,
                this::setSingleXmlCellArray,
                this::insertNewSingleXmlCell,
                this::removeSingleXmlCell,
                this::sizeOfSingleXmlCellArray
            );
        }
    }

    /**
     * Gets array of all "singleXmlCell" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell[] getSingleXmlCellArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell[0]);
    }

    /**
     * Gets ith "singleXmlCell" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell getSingleXmlCellArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "singleXmlCell" element
     */
    @Override
    public int sizeOfSingleXmlCellArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "singleXmlCell" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSingleXmlCellArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell[] singleXmlCellArray) {
        check_orphaned();
        arraySetterHelper(singleXmlCellArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "singleXmlCell" element
     */
    @Override
    public void setSingleXmlCellArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell singleXmlCell) {
        generatedSetterHelperImpl(singleXmlCell, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "singleXmlCell" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell insertNewSingleXmlCell(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "singleXmlCell" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell addNewSingleXmlCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "singleXmlCell" element
     */
    @Override
    public void removeSingleXmlCell(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
