/*
 * XML Type:  CT_AnimationElementChoice
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationElementChoice
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AnimationElementChoice(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTAnimationElementChoiceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationElementChoice {
    private static final long serialVersionUID = 1L;

    public CTAnimationElementChoiceImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "dgm"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "chart"),
    };


    /**
     * Gets the "dgm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmElement getDgm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmElement target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmElement)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dgm" element
     */
    @Override
    public boolean isSetDgm() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "dgm" element
     */
    @Override
    public void setDgm(org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmElement dgm) {
        generatedSetterHelperImpl(dgm, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dgm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmElement addNewDgm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmElement target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmElement)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "dgm" element
     */
    @Override
    public void unsetDgm() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "chart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement getChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "chart" element
     */
    @Override
    public boolean isSetChart() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "chart" element
     */
    @Override
    public void setChart(org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement chart) {
        generatedSetterHelperImpl(chart, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "chart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement addNewChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "chart" element
     */
    @Override
    public void unsetChart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
