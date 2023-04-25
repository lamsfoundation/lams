/*
 * XML Type:  CT_DoughnutChart
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DoughnutChart(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTDoughnutChartImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart {
    private static final long serialVersionUID = 1L;

    public CTDoughnutChartImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "varyColors"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "ser"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "dLbls"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "firstSliceAng"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "holeSize"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "varyColors" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getVaryColors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "varyColors" element
     */
    @Override
    public boolean isSetVaryColors() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "varyColors" element
     */
    @Override
    public void setVaryColors(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean varyColors) {
        generatedSetterHelperImpl(varyColors, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "varyColors" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewVaryColors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "varyColors" element
     */
    @Override
    public void unsetVaryColors() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets a List of "ser" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer> getSerList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSerArray,
                this::setSerArray,
                this::insertNewSer,
                this::removeSer,
                this::sizeOfSerArray
            );
        }
    }

    /**
     * Gets array of all "ser" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer[] getSerArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer[0]);
    }

    /**
     * Gets ith "ser" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer getSerArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ser" element
     */
    @Override
    public int sizeOfSerArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "ser" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSerArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer[] serArray) {
        check_orphaned();
        arraySetterHelper(serArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "ser" element
     */
    @Override
    public void setSerArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer ser) {
        generatedSetterHelperImpl(ser, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ser" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer insertNewSer(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ser" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer addNewSer() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "ser" element
     */
    @Override
    public void removeSer(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets the "dLbls" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls getDLbls() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dLbls" element
     */
    @Override
    public boolean isSetDLbls() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "dLbls" element
     */
    @Override
    public void setDLbls(org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls dLbls) {
        generatedSetterHelperImpl(dLbls, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dLbls" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls addNewDLbls() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "dLbls" element
     */
    @Override
    public void unsetDLbls() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "firstSliceAng" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTFirstSliceAng getFirstSliceAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTFirstSliceAng target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTFirstSliceAng)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "firstSliceAng" element
     */
    @Override
    public boolean isSetFirstSliceAng() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "firstSliceAng" element
     */
    @Override
    public void setFirstSliceAng(org.openxmlformats.schemas.drawingml.x2006.chart.CTFirstSliceAng firstSliceAng) {
        generatedSetterHelperImpl(firstSliceAng, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "firstSliceAng" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTFirstSliceAng addNewFirstSliceAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTFirstSliceAng target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTFirstSliceAng)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "firstSliceAng" element
     */
    @Override
    public void unsetFirstSliceAng() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "holeSize" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTHoleSize getHoleSize() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTHoleSize target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTHoleSize)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "holeSize" element
     */
    @Override
    public boolean isSetHoleSize() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "holeSize" element
     */
    @Override
    public void setHoleSize(org.openxmlformats.schemas.drawingml.x2006.chart.CTHoleSize holeSize) {
        generatedSetterHelperImpl(holeSize, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "holeSize" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTHoleSize addNewHoleSize() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTHoleSize target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTHoleSize)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "holeSize" element
     */
    @Override
    public void unsetHoleSize() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }
}
