/*
 * XML Type:  CT_AdjPoint2D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AdjPoint2D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTAdjPoint2DImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D {
    private static final long serialVersionUID = 1L;

    public CTAdjPoint2DImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "x"),
        new QName("", "y"),
    };


    /**
     * Gets the "x" attribute
     */
    @Override
    public java.lang.Object getX() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "x" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetX() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "x" attribute
     */
    @Override
    public void setX(java.lang.Object x) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(x);
        }
    }

    /**
     * Sets (as xml) the "x" attribute
     */
    @Override
    public void xsetX(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate x) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(x);
        }
    }

    /**
     * Gets the "y" attribute
     */
    @Override
    public java.lang.Object getY() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "y" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetY() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "y" attribute
     */
    @Override
    public void setY(java.lang.Object y) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(y);
        }
    }

    /**
     * Sets (as xml) the "y" attribute
     */
    @Override
    public void xsetY(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate y) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(y);
        }
    }
}
