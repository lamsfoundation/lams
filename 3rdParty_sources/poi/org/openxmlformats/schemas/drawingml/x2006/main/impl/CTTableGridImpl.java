/*
 * XML Type:  CT_TableGrid
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TableGrid(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTableGridImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid {
    private static final long serialVersionUID = 1L;

    public CTTableGridImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gridCol"),
    };


    /**
     * Gets a List of "gridCol" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol> getGridColList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol[] getGridColArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol[0]);
    }

    /**
     * Gets ith "gridCol" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol getGridColArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol)get_store().find_element_user(PROPERTY_QNAME[0], i);
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
    public void setGridColArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol[] gridColArray) {
        check_orphaned();
        arraySetterHelper(gridColArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "gridCol" element
     */
    @Override
    public void setGridColArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol gridCol) {
        generatedSetterHelperImpl(gridCol, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gridCol" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol insertNewGridCol(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "gridCol" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol addNewGridCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol)get_store().add_element_user(PROPERTY_QNAME[0]);
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
