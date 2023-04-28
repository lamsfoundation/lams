/*
 * XML Type:  CT_AnimationGraphicalObjectBuildProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AnimationGraphicalObjectBuildProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTAnimationGraphicalObjectBuildPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties {
    private static final long serialVersionUID = 1L;

    public CTAnimationGraphicalObjectBuildPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "bldDgm"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "bldChart"),
    };


    /**
     * Gets the "bldDgm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmBuildProperties getBldDgm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmBuildProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmBuildProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bldDgm" element
     */
    @Override
    public boolean isSetBldDgm() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "bldDgm" element
     */
    @Override
    public void setBldDgm(org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmBuildProperties bldDgm) {
        generatedSetterHelperImpl(bldDgm, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bldDgm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmBuildProperties addNewBldDgm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmBuildProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmBuildProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "bldDgm" element
     */
    @Override
    public void unsetBldDgm() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "bldChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartBuildProperties getBldChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartBuildProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartBuildProperties)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bldChart" element
     */
    @Override
    public boolean isSetBldChart() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "bldChart" element
     */
    @Override
    public void setBldChart(org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartBuildProperties bldChart) {
        generatedSetterHelperImpl(bldChart, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bldChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartBuildProperties addNewBldChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartBuildProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartBuildProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "bldChart" element
     */
    @Override
    public void unsetBldChart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
