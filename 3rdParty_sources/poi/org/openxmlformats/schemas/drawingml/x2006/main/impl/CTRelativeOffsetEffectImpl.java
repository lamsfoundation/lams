/*
 * XML Type:  CT_RelativeOffsetEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeOffsetEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_RelativeOffsetEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTRelativeOffsetEffectImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeOffsetEffect {
    private static final long serialVersionUID = 1L;

    public CTRelativeOffsetEffectImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "tx"),
        new QName("", "ty"),
    };


    /**
     * Gets the "tx" attribute
     */
    @Override
    public java.lang.Object getTx() {
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
     * Gets (as xml) the "tx" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "tx" attribute
     */
    @Override
    public boolean isSetTx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "tx" attribute
     */
    @Override
    public void setTx(java.lang.Object tx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(tx);
        }
    }

    /**
     * Sets (as xml) the "tx" attribute
     */
    @Override
    public void xsetTx(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage tx) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(tx);
        }
    }

    /**
     * Unsets the "tx" attribute
     */
    @Override
    public void unsetTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "ty" attribute
     */
    @Override
    public java.lang.Object getTy() {
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
     * Gets (as xml) the "ty" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetTy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "ty" attribute
     */
    @Override
    public boolean isSetTy() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "ty" attribute
     */
    @Override
    public void setTy(java.lang.Object ty) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(ty);
        }
    }

    /**
     * Sets (as xml) the "ty" attribute
     */
    @Override
    public void xsetTy(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage ty) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(ty);
        }
    }

    /**
     * Unsets the "ty" attribute
     */
    @Override
    public void unsetTy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}
