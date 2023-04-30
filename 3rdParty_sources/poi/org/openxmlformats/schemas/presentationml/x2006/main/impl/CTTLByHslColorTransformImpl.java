/*
 * XML Type:  CT_TLByHslColorTransform
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLByHslColorTransform(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLByHslColorTransformImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform {
    private static final long serialVersionUID = 1L;

    public CTTLByHslColorTransformImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "h"),
        new QName("", "s"),
        new QName("", "l"),
    };


    /**
     * Gets the "h" attribute
     */
    @Override
    public int getH() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "h" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAngle xgetH() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "h" attribute
     */
    @Override
    public void setH(int h) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setIntValue(h);
        }
    }

    /**
     * Sets (as xml) the "h" attribute
     */
    @Override
    public void xsetH(org.openxmlformats.schemas.drawingml.x2006.main.STAngle h) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(h);
        }
    }

    /**
     * Gets the "s" attribute
     */
    @Override
    public java.lang.Object getS() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "s" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetS() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "s" attribute
     */
    @Override
    public void setS(java.lang.Object s) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(s);
        }
    }

    /**
     * Sets (as xml) the "s" attribute
     */
    @Override
    public void xsetS(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage s) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(s);
        }
    }

    /**
     * Gets the "l" attribute
     */
    @Override
    public java.lang.Object getL() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "l" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetL() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setObjectValue(l);
        }
    }

    /**
     * Sets (as xml) the "l" attribute
     */
    @Override
    public void xsetL(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage l) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(l);
        }
    }
}
