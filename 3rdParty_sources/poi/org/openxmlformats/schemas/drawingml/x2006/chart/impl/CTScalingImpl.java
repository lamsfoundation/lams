/*
 * XML Type:  CT_Scaling
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Scaling(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTScalingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling {
    private static final long serialVersionUID = 1L;

    public CTScalingImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "logBase"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "orientation"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "max"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "min"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "logBase" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase getLogBase() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "logBase" element
     */
    @Override
    public boolean isSetLogBase() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "logBase" element
     */
    @Override
    public void setLogBase(org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase logBase) {
        generatedSetterHelperImpl(logBase, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "logBase" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase addNewLogBase() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "logBase" element
     */
    @Override
    public void unsetLogBase() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "orientation" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation getOrientation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "orientation" element
     */
    @Override
    public boolean isSetOrientation() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "orientation" element
     */
    @Override
    public void setOrientation(org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation orientation) {
        generatedSetterHelperImpl(orientation, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "orientation" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation addNewOrientation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "orientation" element
     */
    @Override
    public void unsetOrientation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "max" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble getMax() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "max" element
     */
    @Override
    public boolean isSetMax() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "max" element
     */
    @Override
    public void setMax(org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble max) {
        generatedSetterHelperImpl(max, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "max" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble addNewMax() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "max" element
     */
    @Override
    public void unsetMax() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "min" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble getMin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "min" element
     */
    @Override
    public boolean isSetMin() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "min" element
     */
    @Override
    public void setMin(org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble min) {
        generatedSetterHelperImpl(min, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "min" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble addNewMin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "min" element
     */
    @Override
    public void unsetMin() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[4], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }
}
