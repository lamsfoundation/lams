/*
 * XML Type:  CT_AnimationChartElement
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AnimationChartElement(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTAnimationChartElementImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement {
    private static final long serialVersionUID = 1L;

    public CTAnimationChartElementImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "seriesIdx"),
        new QName("", "categoryIdx"),
        new QName("", "bldStep"),
    };


    /**
     * Gets the "seriesIdx" attribute
     */
    @Override
    public int getSeriesIdx() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "seriesIdx" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetSeriesIdx() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "seriesIdx" attribute
     */
    @Override
    public boolean isSetSeriesIdx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "seriesIdx" attribute
     */
    @Override
    public void setSeriesIdx(int seriesIdx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setIntValue(seriesIdx);
        }
    }

    /**
     * Sets (as xml) the "seriesIdx" attribute
     */
    @Override
    public void xsetSeriesIdx(org.apache.xmlbeans.XmlInt seriesIdx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(seriesIdx);
        }
    }

    /**
     * Unsets the "seriesIdx" attribute
     */
    @Override
    public void unsetSeriesIdx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "categoryIdx" attribute
     */
    @Override
    public int getCategoryIdx() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "categoryIdx" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetCategoryIdx() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "categoryIdx" attribute
     */
    @Override
    public boolean isSetCategoryIdx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "categoryIdx" attribute
     */
    @Override
    public void setCategoryIdx(int categoryIdx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setIntValue(categoryIdx);
        }
    }

    /**
     * Sets (as xml) the "categoryIdx" attribute
     */
    @Override
    public void xsetCategoryIdx(org.apache.xmlbeans.XmlInt categoryIdx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(categoryIdx);
        }
    }

    /**
     * Unsets the "categoryIdx" attribute
     */
    @Override
    public void unsetCategoryIdx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "bldStep" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep.Enum getBldStep() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "bldStep" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep xgetBldStep() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "bldStep" attribute
     */
    @Override
    public void setBldStep(org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep.Enum bldStep) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(bldStep);
        }
    }

    /**
     * Sets (as xml) the "bldStep" attribute
     */
    @Override
    public void xsetBldStep(org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep bldStep) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(bldStep);
        }
    }
}
