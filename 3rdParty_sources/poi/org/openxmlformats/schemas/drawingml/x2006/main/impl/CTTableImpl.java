/*
 * XML Type:  CT_Table
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTable
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Table(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTableImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTable {
    private static final long serialVersionUID = 1L;

    public CTTableImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tblPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tblGrid"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tr"),
    };


    /**
     * Gets the "tblPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties getTblPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblPr" element
     */
    @Override
    public boolean isSetTblPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "tblPr" element
     */
    @Override
    public void setTblPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties tblPr) {
        generatedSetterHelperImpl(tblPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties addNewTblPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "tblPr" element
     */
    @Override
    public void unsetTblPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "tblGrid" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid getTblGrid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "tblGrid" element
     */
    @Override
    public void setTblGrid(org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid tblGrid) {
        generatedSetterHelperImpl(tblGrid, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblGrid" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid addNewTblGrid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets a List of "tr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow> getTrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTrArray,
                this::setTrArray,
                this::insertNewTr,
                this::removeTr,
                this::sizeOfTrArray
            );
        }
    }

    /**
     * Gets array of all "tr" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow[] getTrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow[0]);
    }

    /**
     * Gets ith "tr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow getTrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tr" element
     */
    @Override
    public int sizeOfTrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "tr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTrArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow[] trArray) {
        check_orphaned();
        arraySetterHelper(trArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "tr" element
     */
    @Override
    public void setTrArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow tr) {
        generatedSetterHelperImpl(tr, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow insertNewTr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow addNewTr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "tr" element
     */
    @Override
    public void removeTr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }
}
