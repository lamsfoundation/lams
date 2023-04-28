/*
 * XML Type:  CT_TblGridBase
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TblGridBase(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTblGridBaseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase {
    private static final long serialVersionUID = 1L;

    public CTTblGridBaseImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "gridCol"),
    };


    /**
     * Gets a List of "gridCol" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol> getGridColList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGridColArray,
                this::setGridColArray,
                this::insertNewGridCol,
                this::removeGridCol,
                this::sizeOfGridColArray
            );
        }
    }

    /**
     * Gets array of all "gridCol" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol[] getGridColArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol[0]);
    }

    /**
     * Gets ith "gridCol" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol getGridColArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "gridCol" element
     */
    @Override
    public int sizeOfGridColArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "gridCol" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGridColArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol[] gridColArray) {
        check_orphaned();
        arraySetterHelper(gridColArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "gridCol" element
     */
    @Override
    public void setGridColArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol gridCol) {
        generatedSetterHelperImpl(gridCol, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gridCol" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol insertNewGridCol(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "gridCol" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol addNewGridCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "gridCol" element
     */
    @Override
    public void removeGridCol(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
