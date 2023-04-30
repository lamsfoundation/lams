/*
 * XML Type:  CT_AxDataSource
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AxDataSource(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTAxDataSourceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource {
    private static final long serialVersionUID = 1L;

    public CTAxDataSourceImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "multiLvlStrRef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "numRef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "numLit"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "strRef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "strLit"),
    };


    /**
     * Gets the "multiLvlStrRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrRef getMultiLvlStrRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrRef target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrRef)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "multiLvlStrRef" element
     */
    @Override
    public boolean isSetMultiLvlStrRef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "multiLvlStrRef" element
     */
    @Override
    public void setMultiLvlStrRef(org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrRef multiLvlStrRef) {
        generatedSetterHelperImpl(multiLvlStrRef, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "multiLvlStrRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrRef addNewMultiLvlStrRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrRef target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrRef)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "multiLvlStrRef" element
     */
    @Override
    public void unsetMultiLvlStrRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "numRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef getNumRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numRef" element
     */
    @Override
    public boolean isSetNumRef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "numRef" element
     */
    @Override
    public void setNumRef(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef numRef) {
        generatedSetterHelperImpl(numRef, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef addNewNumRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "numRef" element
     */
    @Override
    public void unsetNumRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "numLit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData getNumLit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numLit" element
     */
    @Override
    public boolean isSetNumLit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "numLit" element
     */
    @Override
    public void setNumLit(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData numLit) {
        generatedSetterHelperImpl(numLit, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numLit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData addNewNumLit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "numLit" element
     */
    @Override
    public void unsetNumLit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "strRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef getStrRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "strRef" element
     */
    @Override
    public boolean isSetStrRef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "strRef" element
     */
    @Override
    public void setStrRef(org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef strRef) {
        generatedSetterHelperImpl(strRef, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "strRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef addNewStrRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "strRef" element
     */
    @Override
    public void unsetStrRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "strLit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTStrData getStrLit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTStrData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTStrData)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "strLit" element
     */
    @Override
    public boolean isSetStrLit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "strLit" element
     */
    @Override
    public void setStrLit(org.openxmlformats.schemas.drawingml.x2006.chart.CTStrData strLit) {
        generatedSetterHelperImpl(strLit, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "strLit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTStrData addNewStrLit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTStrData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTStrData)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "strLit" element
     */
    @Override
    public void unsetStrLit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }
}
