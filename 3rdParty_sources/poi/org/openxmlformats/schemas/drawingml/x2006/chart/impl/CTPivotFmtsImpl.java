/*
 * XML Type:  CT_PivotFmts
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmts
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PivotFmts(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTPivotFmtsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmts {
    private static final long serialVersionUID = 1L;

    public CTPivotFmtsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "pivotFmt"),
    };


    /**
     * Gets a List of "pivotFmt" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt> getPivotFmtList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPivotFmtArray,
                this::setPivotFmtArray,
                this::insertNewPivotFmt,
                this::removePivotFmt,
                this::sizeOfPivotFmtArray
            );
        }
    }

    /**
     * Gets array of all "pivotFmt" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt[] getPivotFmtArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt[0]);
    }

    /**
     * Gets ith "pivotFmt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt getPivotFmtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "pivotFmt" element
     */
    @Override
    public int sizeOfPivotFmtArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "pivotFmt" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPivotFmtArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt[] pivotFmtArray) {
        check_orphaned();
        arraySetterHelper(pivotFmtArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "pivotFmt" element
     */
    @Override
    public void setPivotFmtArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt pivotFmt) {
        generatedSetterHelperImpl(pivotFmt, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pivotFmt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt insertNewPivotFmt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "pivotFmt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt addNewPivotFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmt)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "pivotFmt" element
     */
    @Override
    public void removePivotFmt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
