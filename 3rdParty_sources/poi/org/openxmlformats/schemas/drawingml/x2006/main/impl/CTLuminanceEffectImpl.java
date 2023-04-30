/*
 * XML Type:  CT_LuminanceEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_LuminanceEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTLuminanceEffectImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect {
    private static final long serialVersionUID = 1L;

    public CTLuminanceEffectImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "bright"),
        new QName("", "contrast"),
    };


    /**
     * Gets the "bright" attribute
     */
    @Override
    public java.lang.Object getBright() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "bright" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetBright() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "bright" attribute
     */
    @Override
    public boolean isSetBright() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "bright" attribute
     */
    @Override
    public void setBright(java.lang.Object bright) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(bright);
        }
    }

    /**
     * Sets (as xml) the "bright" attribute
     */
    @Override
    public void xsetBright(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage bright) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(bright);
        }
    }

    /**
     * Unsets the "bright" attribute
     */
    @Override
    public void unsetBright() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "contrast" attribute
     */
    @Override
    public java.lang.Object getContrast() {
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
     * Gets (as xml) the "contrast" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetContrast() {
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
     * True if has "contrast" attribute
     */
    @Override
    public boolean isSetContrast() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "contrast" attribute
     */
    @Override
    public void setContrast(java.lang.Object contrast) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(contrast);
        }
    }

    /**
     * Sets (as xml) the "contrast" attribute
     */
    @Override
    public void xsetContrast(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage contrast) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(contrast);
        }
    }

    /**
     * Unsets the "contrast" attribute
     */
    @Override
    public void unsetContrast() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}
