/*
 * XML Type:  CT_PictureOptions
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureOptions
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PictureOptions(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTPictureOptionsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureOptions {
    private static final long serialVersionUID = 1L;

    public CTPictureOptionsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "applyToFront"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "applyToSides"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "applyToEnd"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "pictureFormat"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "pictureStackUnit"),
    };


    /**
     * Gets the "applyToFront" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getApplyToFront() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "applyToFront" element
     */
    @Override
    public boolean isSetApplyToFront() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "applyToFront" element
     */
    @Override
    public void setApplyToFront(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean applyToFront) {
        generatedSetterHelperImpl(applyToFront, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "applyToFront" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewApplyToFront() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "applyToFront" element
     */
    @Override
    public void unsetApplyToFront() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "applyToSides" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getApplyToSides() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "applyToSides" element
     */
    @Override
    public boolean isSetApplyToSides() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "applyToSides" element
     */
    @Override
    public void setApplyToSides(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean applyToSides) {
        generatedSetterHelperImpl(applyToSides, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "applyToSides" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewApplyToSides() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "applyToSides" element
     */
    @Override
    public void unsetApplyToSides() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "applyToEnd" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getApplyToEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "applyToEnd" element
     */
    @Override
    public boolean isSetApplyToEnd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "applyToEnd" element
     */
    @Override
    public void setApplyToEnd(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean applyToEnd) {
        generatedSetterHelperImpl(applyToEnd, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "applyToEnd" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewApplyToEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "applyToEnd" element
     */
    @Override
    public void unsetApplyToEnd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "pictureFormat" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureFormat getPictureFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureFormat target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureFormat)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pictureFormat" element
     */
    @Override
    public boolean isSetPictureFormat() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "pictureFormat" element
     */
    @Override
    public void setPictureFormat(org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureFormat pictureFormat) {
        generatedSetterHelperImpl(pictureFormat, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pictureFormat" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureFormat addNewPictureFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureFormat target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureFormat)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "pictureFormat" element
     */
    @Override
    public void unsetPictureFormat() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "pictureStackUnit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureStackUnit getPictureStackUnit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureStackUnit target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureStackUnit)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pictureStackUnit" element
     */
    @Override
    public boolean isSetPictureStackUnit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "pictureStackUnit" element
     */
    @Override
    public void setPictureStackUnit(org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureStackUnit pictureStackUnit) {
        generatedSetterHelperImpl(pictureStackUnit, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pictureStackUnit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureStackUnit addNewPictureStackUnit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureStackUnit target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureStackUnit)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "pictureStackUnit" element
     */
    @Override
    public void unsetPictureStackUnit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }
}
