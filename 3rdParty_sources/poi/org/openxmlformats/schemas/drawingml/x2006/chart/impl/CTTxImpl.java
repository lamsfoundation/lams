/*
 * XML Type:  CT_Tx
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTTx
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Tx(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTTxImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTTx {
    private static final long serialVersionUID = 1L;

    public CTTxImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "strRef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "rich"),
    };


    /**
     * Gets the "strRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef getStrRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "strRef" element
     */
    @Override
    public void setStrRef(org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef strRef) {
        generatedSetterHelperImpl(strRef, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "strRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef addNewStrRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "rich" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody getRich() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rich" element
     */
    @Override
    public boolean isSetRich() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "rich" element
     */
    @Override
    public void setRich(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody rich) {
        generatedSetterHelperImpl(rich, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rich" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody addNewRich() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "rich" element
     */
    @Override
    public void unsetRich() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
