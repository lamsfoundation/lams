/*
 * XML Type:  CT_BandFmts
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmts
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_BandFmts(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTBandFmtsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmts {
    private static final long serialVersionUID = 1L;

    public CTBandFmtsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "bandFmt"),
    };


    /**
     * Gets a List of "bandFmt" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt> getBandFmtList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBandFmtArray,
                this::setBandFmtArray,
                this::insertNewBandFmt,
                this::removeBandFmt,
                this::sizeOfBandFmtArray
            );
        }
    }

    /**
     * Gets array of all "bandFmt" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt[] getBandFmtArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt[0]);
    }

    /**
     * Gets ith "bandFmt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt getBandFmtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bandFmt" element
     */
    @Override
    public int sizeOfBandFmtArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "bandFmt" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBandFmtArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt[] bandFmtArray) {
        check_orphaned();
        arraySetterHelper(bandFmtArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "bandFmt" element
     */
    @Override
    public void setBandFmtArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt bandFmt) {
        generatedSetterHelperImpl(bandFmt, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bandFmt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt insertNewBandFmt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bandFmt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt addNewBandFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBandFmt)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "bandFmt" element
     */
    @Override
    public void removeBandFmt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
