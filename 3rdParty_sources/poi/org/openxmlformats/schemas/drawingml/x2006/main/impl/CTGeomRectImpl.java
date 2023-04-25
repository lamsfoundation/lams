/*
 * XML Type:  CT_GeomRect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GeomRect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTGeomRectImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect {
    private static final long serialVersionUID = 1L;

    public CTGeomRectImpl(org.apache.xmlbeans.SchemaType sType) {
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
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "l" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetL() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[0]);
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
    public void xsetL(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate l) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(l);
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
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "t" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetT() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
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
    public void xsetT(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate t) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(t);
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
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "r" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[2]);
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
    public void xsetR(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate r) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(r);
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
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "b" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetB() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[3]);
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
    public void xsetB(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate b) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(b);
        }
    }
}
