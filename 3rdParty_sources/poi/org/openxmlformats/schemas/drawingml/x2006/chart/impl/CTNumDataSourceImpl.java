/*
 * XML Type:  CT_NumDataSource
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_NumDataSource(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTNumDataSourceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource {
    private static final long serialVersionUID = 1L;

    public CTNumDataSourceImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "numRef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "numLit"),
    };


    /**
     * Gets the "numRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef getNumRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "numRef" element
     */
    @Override
    public void setNumRef(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef numRef) {
        generatedSetterHelperImpl(numRef, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef addNewNumRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "numLit" element
     */
    @Override
    public void setNumLit(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData numLit) {
        generatedSetterHelperImpl(numLit, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numLit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData addNewNumLit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
