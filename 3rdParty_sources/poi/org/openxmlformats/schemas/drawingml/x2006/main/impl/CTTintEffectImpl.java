/*
 * XML Type:  CT_TintEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TintEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTintEffectImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect {
    private static final long serialVersionUID = 1L;

    public CTTintEffectImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "hue"),
        new QName("", "amt"),
    };


    /**
     * Gets the "hue" attribute
     */
    @Override
    public int getHue() {
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
     * Gets (as xml) the "hue" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle xgetHue() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "hue" attribute
     */
    @Override
    public boolean isSetHue() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "hue" attribute
     */
    @Override
    public void setHue(int hue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setIntValue(hue);
        }
    }

    /**
     * Sets (as xml) the "hue" attribute
     */
    @Override
    public void xsetHue(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle hue) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(hue);
        }
    }

    /**
     * Unsets the "hue" attribute
     */
    @Override
    public void unsetHue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "amt" attribute
     */
    @Override
    public java.lang.Object getAmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "amt" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetAmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "amt" attribute
     */
    @Override
    public boolean isSetAmt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "amt" attribute
     */
    @Override
    public void setAmt(java.lang.Object amt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(amt);
        }
    }

    /**
     * Sets (as xml) the "amt" attribute
     */
    @Override
    public void xsetAmt(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage amt) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(amt);
        }
    }

    /**
     * Unsets the "amt" attribute
     */
    @Override
    public void unsetAmt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}
