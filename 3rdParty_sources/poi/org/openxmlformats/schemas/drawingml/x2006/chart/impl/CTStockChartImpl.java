/*
 * XML Type:  CT_StockChart
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_StockChart(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTStockChartImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart {
    private static final long serialVersionUID = 1L;

    public CTStockChartImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "ser"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "dLbls"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "dropLines"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "hiLowLines"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "upDownBars"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "axId"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets a List of "ser" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer> getSerList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer[] getSerArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer[0]);
    }

    /**
     * Gets ith "ser" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer getSerArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer)get_store().find_element_user(PROPERTY_QNAME[0], i);
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
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "ser" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSerArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer[] serArray) {
        check_orphaned();
        arraySetterHelper(serArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "ser" element
     */
    @Override
    public void setSerArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer ser) {
        generatedSetterHelperImpl(ser, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ser" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer insertNewSer(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ser" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer addNewSer() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], i);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "dLbls" element
     */
    @Override
    public void setDLbls(org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls dLbls) {
        generatedSetterHelperImpl(dLbls, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dLbls" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls addNewDLbls() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "dropLines" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines getDropLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dropLines" element
     */
    @Override
    public boolean isSetDropLines() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "dropLines" element
     */
    @Override
    public void setDropLines(org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines dropLines) {
        generatedSetterHelperImpl(dropLines, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dropLines" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines addNewDropLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "dropLines" element
     */
    @Override
    public void unsetDropLines() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "hiLowLines" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines getHiLowLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hiLowLines" element
     */
    @Override
    public boolean isSetHiLowLines() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "hiLowLines" element
     */
    @Override
    public void setHiLowLines(org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines hiLowLines) {
        generatedSetterHelperImpl(hiLowLines, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hiLowLines" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines addNewHiLowLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "hiLowLines" element
     */
    @Override
    public void unsetHiLowLines() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "upDownBars" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars getUpDownBars() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "upDownBars" element
     */
    @Override
    public boolean isSetUpDownBars() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "upDownBars" element
     */
    @Override
    public void setUpDownBars(org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars upDownBars) {
        generatedSetterHelperImpl(upDownBars, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "upDownBars" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars addNewUpDownBars() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "upDownBars" element
     */
    @Override
    public void unsetUpDownBars() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets a List of "axId" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt> getAxIdList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAxIdArray,
                this::setAxIdArray,
                this::insertNewAxId,
                this::removeAxId,
                this::sizeOfAxIdArray
            );
        }
    }

    /**
     * Gets array of all "axId" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt[] getAxIdArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt[0]);
    }

    /**
     * Gets ith "axId" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getAxIdArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "axId" element
     */
    @Override
    public int sizeOfAxIdArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "axId" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAxIdArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt[] axIdArray) {
        check_orphaned();
        arraySetterHelper(axIdArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "axId" element
     */
    @Override
    public void setAxIdArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt axId) {
        generatedSetterHelperImpl(axId, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "axId" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt insertNewAxId(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "axId" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewAxId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "axId" element
     */
    @Override
    public void removeAxId(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[6], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[6]);
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
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }
}
