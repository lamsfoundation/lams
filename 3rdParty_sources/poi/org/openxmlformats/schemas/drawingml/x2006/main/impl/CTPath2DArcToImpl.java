/*
 * XML Type:  CT_Path2DArcTo
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Path2DArcTo(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPath2DArcToImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo {
    private static final long serialVersionUID = 1L;

    public CTPath2DArcToImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "wR"),
        new QName("", "hR"),
        new QName("", "stAng"),
        new QName("", "swAng"),
    };


    /**
     * Gets the "wR" attribute
     */
    @Override
    public java.lang.Object getWR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "wR" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetWR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "wR" attribute
     */
    @Override
    public void setWR(java.lang.Object wr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(wr);
        }
    }

    /**
     * Sets (as xml) the "wR" attribute
     */
    @Override
    public void xsetWR(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate wr) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(wr);
        }
    }

    /**
     * Gets the "hR" attribute
     */
    @Override
    public java.lang.Object getHR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "hR" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetHR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "hR" attribute
     */
    @Override
    public void setHR(java.lang.Object hr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(hr);
        }
    }

    /**
     * Sets (as xml) the "hR" attribute
     */
    @Override
    public void xsetHR(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate hr) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(hr);
        }
    }

    /**
     * Gets the "stAng" attribute
     */
    @Override
    public java.lang.Object getStAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "stAng" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle xgetStAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "stAng" attribute
     */
    @Override
    public void setStAng(java.lang.Object stAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setObjectValue(stAng);
        }
    }

    /**
     * Sets (as xml) the "stAng" attribute
     */
    @Override
    public void xsetStAng(org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle stAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(stAng);
        }
    }

    /**
     * Gets the "swAng" attribute
     */
    @Override
    public java.lang.Object getSwAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "swAng" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle xgetSwAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Sets the "swAng" attribute
     */
    @Override
    public void setSwAng(java.lang.Object swAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setObjectValue(swAng);
        }
    }

    /**
     * Sets (as xml) the "swAng" attribute
     */
    @Override
    public void xsetSwAng(org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle swAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(swAng);
        }
    }
}
