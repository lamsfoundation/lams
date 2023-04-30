/*
 * XML Type:  CT_UpDownBars
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_UpDownBars(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTUpDownBarsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars {
    private static final long serialVersionUID = 1L;

    public CTUpDownBarsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "gapWidth"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "upBars"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "downBars"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "gapWidth" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTGapAmount getGapWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTGapAmount target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTGapAmount)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "gapWidth" element
     */
    @Override
    public boolean isSetGapWidth() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "gapWidth" element
     */
    @Override
    public void setGapWidth(org.openxmlformats.schemas.drawingml.x2006.chart.CTGapAmount gapWidth) {
        generatedSetterHelperImpl(gapWidth, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "gapWidth" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTGapAmount addNewGapWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTGapAmount target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTGapAmount)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "gapWidth" element
     */
    @Override
    public void unsetGapWidth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "upBars" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar getUpBars() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "upBars" element
     */
    @Override
    public boolean isSetUpBars() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "upBars" element
     */
    @Override
    public void setUpBars(org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar upBars) {
        generatedSetterHelperImpl(upBars, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "upBars" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar addNewUpBars() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "upBars" element
     */
    @Override
    public void unsetUpBars() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "downBars" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar getDownBars() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "downBars" element
     */
    @Override
    public boolean isSetDownBars() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "downBars" element
     */
    @Override
    public void setDownBars(org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar downBars) {
        generatedSetterHelperImpl(downBars, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "downBars" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar addNewDownBars() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBar)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "downBars" element
     */
    @Override
    public void unsetDownBars() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}
