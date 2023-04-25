/*
 * XML Type:  CT_CellWatches
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatches
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CellWatches(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTCellWatchesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatches {
    private static final long serialVersionUID = 1L;

    public CTCellWatchesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cellWatch"),
    };


    /**
     * Gets a List of "cellWatch" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch> getCellWatchList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCellWatchArray,
                this::setCellWatchArray,
                this::insertNewCellWatch,
                this::removeCellWatch,
                this::sizeOfCellWatchArray
            );
        }
    }

    /**
     * Gets array of all "cellWatch" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch[] getCellWatchArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch[0]);
    }

    /**
     * Gets ith "cellWatch" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch getCellWatchArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cellWatch" element
     */
    @Override
    public int sizeOfCellWatchArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cellWatch" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCellWatchArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch[] cellWatchArray) {
        check_orphaned();
        arraySetterHelper(cellWatchArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cellWatch" element
     */
    @Override
    public void setCellWatchArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch cellWatch) {
        generatedSetterHelperImpl(cellWatch, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cellWatch" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch insertNewCellWatch(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cellWatch" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch addNewCellWatch() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cellWatch" element
     */
    @Override
    public void removeCellWatch(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
