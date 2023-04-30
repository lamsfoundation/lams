/*
 * XML Type:  CT_RelativeRect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_RelativeRect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTRelativeRectImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect {
    private static final long serialVersionUID = 1L;

    public CTRelativeRectImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "l"),
        new QName("", "t"),
        new QName("", "r"),
        new QName("", "b"),
    };


    /**
     * Gets the "l" attribute
     */
    @Override
    public java.lang.Object getL() {
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
     * Gets (as xml) the "l" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetL() {
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
     * True if has "l" attribute
     */
    @Override
    public boolean isSetL() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "l" attribute
     */
    @Override
    public void setL(java.lang.Object l) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(l);
        }
    }

    /**
     * Sets (as xml) the "l" attribute
     */
    @Override
    public void xsetL(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage l) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(l);
        }
    }

    /**
     * Unsets the "l" attribute
     */
    @Override
    public void unsetL() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "t" attribute
     */
    @Override
    public java.lang.Object getT() {
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
     * Gets (as xml) the "t" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetT() {
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
     * True if has "t" attribute
     */
    @Override
    public boolean isSetT() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "t" attribute
     */
    @Override
    public void setT(java.lang.Object t) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(t);
        }
    }

    /**
     * Sets (as xml) the "t" attribute
     */
    @Override
    public void xsetT(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage t) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(t);
        }
    }

    /**
     * Unsets the "t" attribute
     */
    @Override
    public void unsetT() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "r" attribute
     */
    @Override
    public java.lang.Object getR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "r" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "r" attribute
     */
    @Override
    public boolean isSetR() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "r" attribute
     */
    @Override
    public void setR(java.lang.Object r) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setObjectValue(r);
        }
    }

    /**
     * Sets (as xml) the "r" attribute
     */
    @Override
    public void xsetR(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage r) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(r);
        }
    }

    /**
     * Unsets the "r" attribute
     */
    @Override
    public void unsetR() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "b" attribute
     */
    @Override
    public java.lang.Object getB() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "b" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetB() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "b" attribute
     */
    @Override
    public boolean isSetB() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "b" attribute
     */
    @Override
    public void setB(java.lang.Object b) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setObjectValue(b);
        }
    }

    /**
     * Sets (as xml) the "b" attribute
     */
    @Override
    public void xsetB(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage b) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(b);
        }
    }

    /**
     * Unsets the "b" attribute
     */
    @Override
    public void unsetB() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}
