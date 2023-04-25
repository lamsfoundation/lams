/*
 * XML Type:  CT_TLByRgbColorTransform
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLByRgbColorTransform
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLByRgbColorTransform(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLByRgbColorTransformImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLByRgbColorTransform {
    private static final long serialVersionUID = 1L;

    public CTTLByRgbColorTransformImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "r"),
        new QName("", "g"),
        new QName("", "b"),
    };


    /**
     * Gets the "r" attribute
     */
    @Override
    public java.lang.Object getR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "r" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(r);
        }
    }

    /**
     * Sets (as xml) the "r" attribute
     */
    @Override
    public void xsetR(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage r) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(r);
        }
    }

    /**
     * Gets the "g" attribute
     */
    @Override
    public java.lang.Object getG() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "g" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetG() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "g" attribute
     */
    @Override
    public void setG(java.lang.Object g) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(g);
        }
    }

    /**
     * Sets (as xml) the "g" attribute
     */
    @Override
    public void xsetG(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage g) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(g);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "b" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetB() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setObjectValue(b);
        }
    }

    /**
     * Sets (as xml) the "b" attribute
     */
    @Override
    public void xsetB(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage b) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(b);
        }
    }
}
