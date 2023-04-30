/*
 * XML Type:  CT_ScatterChart
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ScatterChart(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTScatterChartImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart {
    private static final long serialVersionUID = 1L;

    public CTScatterChartImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "scatterStyle"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "varyColors"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "ser"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "dLbls"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "axId"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "scatterStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle getScatterStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "scatterStyle" element
     */
    @Override
    public void setScatterStyle(org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle scatterStyle) {
        generatedSetterHelperImpl(scatterStyle, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "scatterStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle addNewScatterStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "varyColors" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getVaryColors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "varyColors" element
     */
    @Override
    public void setVaryColors(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean varyColors) {
        generatedSetterHelperImpl(varyColors, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "varyColors" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewVaryColors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets a List of "ser" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer> getSerList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer[] getSerArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer[0]);
    }

    /**
     * Gets ith "ser" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer getSerArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer)get_store().find_element_user(PROPERTY_QNAME[2], i);
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
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "ser" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSerArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer[] serArray) {
        check_orphaned();
        arraySetterHelper(serArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "ser" element
     */
    @Override
    public void setSerArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer ser) {
        generatedSetterHelperImpl(ser, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ser" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer insertNewSer(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ser" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer addNewSer() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], i);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "dLbls" element
     */
    @Override
    public void setDLbls(org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls dLbls) {
        generatedSetterHelperImpl(dLbls, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dLbls" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls addNewDLbls() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
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
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt[0]);
    }

    /**
     * Gets ith "axId" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getAxIdArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().find_element_user(PROPERTY_QNAME[4], i);
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
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "axId" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAxIdArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt[] axIdArray) {
        check_orphaned();
        arraySetterHelper(axIdArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "axId" element
     */
    @Override
    public void setAxIdArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt axId) {
        generatedSetterHelperImpl(axId, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "axId" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt insertNewAxId(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().insert_element_user(PROPERTY_QNAME[4], i);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], i);
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
