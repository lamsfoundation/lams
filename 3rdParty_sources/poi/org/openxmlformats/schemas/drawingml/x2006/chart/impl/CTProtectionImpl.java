/*
 * XML Type:  CT_Protection
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTProtection
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Protection(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTProtectionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTProtection {
    private static final long serialVersionUID = 1L;

    public CTProtectionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "chartObject"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "data"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "formatting"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "selection"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "userInterface"),
    };


    /**
     * Gets the "chartObject" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getChartObject() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "chartObject" element
     */
    @Override
    public boolean isSetChartObject() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "chartObject" element
     */
    @Override
    public void setChartObject(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean chartObject) {
        generatedSetterHelperImpl(chartObject, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "chartObject" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewChartObject() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "chartObject" element
     */
    @Override
    public void unsetChartObject() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "data" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "data" element
     */
    @Override
    public boolean isSetData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "data" element
     */
    @Override
    public void setData(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean data) {
        generatedSetterHelperImpl(data, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "data" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "data" element
     */
    @Override
    public void unsetData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "formatting" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "formatting" element
     */
    @Override
    public boolean isSetFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "formatting" element
     */
    @Override
    public void setFormatting(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean formatting) {
        generatedSetterHelperImpl(formatting, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "formatting" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "formatting" element
     */
    @Override
    public void unsetFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "selection" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getSelection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "selection" element
     */
    @Override
    public boolean isSetSelection() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "selection" element
     */
    @Override
    public void setSelection(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean selection) {
        generatedSetterHelperImpl(selection, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "selection" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewSelection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "selection" element
     */
    @Override
    public void unsetSelection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "userInterface" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getUserInterface() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "userInterface" element
     */
    @Override
    public boolean isSetUserInterface() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "userInterface" element
     */
    @Override
    public void setUserInterface(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean userInterface) {
        generatedSetterHelperImpl(userInterface, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "userInterface" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewUserInterface() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "userInterface" element
     */
    @Override
    public void unsetUserInterface() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }
}
