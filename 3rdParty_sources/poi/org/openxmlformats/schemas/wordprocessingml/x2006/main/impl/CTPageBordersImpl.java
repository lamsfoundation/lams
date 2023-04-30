/*
 * XML Type:  CT_PageBorders
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorders
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PageBorders(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPageBordersImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorders {
    private static final long serialVersionUID = 1L;

    public CTPageBordersImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "top"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "left"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bottom"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "right"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "zOrder"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "display"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "offsetFrom"),
    };


    /**
     * Gets the "top" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTopPageBorder getTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTopPageBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTopPageBorder)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "top" element
     */
    @Override
    public boolean isSetTop() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "top" element
     */
    @Override
    public void setTop(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTopPageBorder top) {
        generatedSetterHelperImpl(top, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "top" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTopPageBorder addNewTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTopPageBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTopPageBorder)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "top" element
     */
    @Override
    public void unsetTop() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "left" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder getLeft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "left" element
     */
    @Override
    public boolean isSetLeft() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "left" element
     */
    @Override
    public void setLeft(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder left) {
        generatedSetterHelperImpl(left, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "left" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder addNewLeft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "left" element
     */
    @Override
    public void unsetLeft() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "bottom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBottomPageBorder getBottom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBottomPageBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBottomPageBorder)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bottom" element
     */
    @Override
    public boolean isSetBottom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "bottom" element
     */
    @Override
    public void setBottom(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBottomPageBorder bottom) {
        generatedSetterHelperImpl(bottom, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bottom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBottomPageBorder addNewBottom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBottomPageBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBottomPageBorder)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "bottom" element
     */
    @Override
    public void unsetBottom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "right" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder getRight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "right" element
     */
    @Override
    public boolean isSetRight() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "right" element
     */
    @Override
    public void setRight(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder right) {
        generatedSetterHelperImpl(right, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "right" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder addNewRight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "right" element
     */
    @Override
    public void unsetRight() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "zOrder" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder.Enum getZOrder() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "zOrder" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder xgetZOrder() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "zOrder" attribute
     */
    @Override
    public boolean isSetZOrder() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "zOrder" attribute
     */
    @Override
    public void setZOrder(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder.Enum zOrder) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(zOrder);
        }
    }

    /**
     * Sets (as xml) the "zOrder" attribute
     */
    @Override
    public void xsetZOrder(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder zOrder) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(zOrder);
        }
    }

    /**
     * Unsets the "zOrder" attribute
     */
    @Override
    public void unsetZOrder() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "display" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay.Enum getDisplay() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "display" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay xgetDisplay() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "display" attribute
     */
    @Override
    public boolean isSetDisplay() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "display" attribute
     */
    @Override
    public void setDisplay(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay.Enum display) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(display);
        }
    }

    /**
     * Sets (as xml) the "display" attribute
     */
    @Override
    public void xsetDisplay(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay display) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(display);
        }
    }

    /**
     * Unsets the "display" attribute
     */
    @Override
    public void unsetDisplay() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "offsetFrom" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset.Enum getOffsetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "offsetFrom" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset xgetOffsetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return target;
        }
    }

    /**
     * True if has "offsetFrom" attribute
     */
    @Override
    public boolean isSetOffsetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "offsetFrom" attribute
     */
    @Override
    public void setOffsetFrom(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset.Enum offsetFrom) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(offsetFrom);
        }
    }

    /**
     * Sets (as xml) the "offsetFrom" attribute
     */
    @Override
    public void xsetOffsetFrom(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset offsetFrom) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(offsetFrom);
        }
    }

    /**
     * Unsets the "offsetFrom" attribute
     */
    @Override
    public void unsetOffsetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }
}
