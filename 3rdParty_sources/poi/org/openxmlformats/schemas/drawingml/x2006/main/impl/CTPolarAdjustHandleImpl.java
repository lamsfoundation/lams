/*
 * XML Type:  CT_PolarAdjustHandle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PolarAdjustHandle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPolarAdjustHandleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle {
    private static final long serialVersionUID = 1L;

    public CTPolarAdjustHandleImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "pos"),
        new QName("", "gdRefR"),
        new QName("", "minR"),
        new QName("", "maxR"),
        new QName("", "gdRefAng"),
        new QName("", "minAng"),
        new QName("", "maxAng"),
    };


    /**
     * Gets the "pos" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D getPos() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "pos" element
     */
    @Override
    public void setPos(org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D pos) {
        generatedSetterHelperImpl(pos, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pos" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D addNewPos() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "gdRefR" attribute
     */
    @Override
    public java.lang.String getGdRefR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "gdRefR" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName xgetGdRefR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "gdRefR" attribute
     */
    @Override
    public boolean isSetGdRefR() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "gdRefR" attribute
     */
    @Override
    public void setGdRefR(java.lang.String gdRefR) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(gdRefR);
        }
    }

    /**
     * Sets (as xml) the "gdRefR" attribute
     */
    @Override
    public void xsetGdRefR(org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName gdRefR) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(gdRefR);
        }
    }

    /**
     * Unsets the "gdRefR" attribute
     */
    @Override
    public void unsetGdRefR() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "minR" attribute
     */
    @Override
    public java.lang.Object getMinR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "minR" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetMinR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "minR" attribute
     */
    @Override
    public boolean isSetMinR() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "minR" attribute
     */
    @Override
    public void setMinR(java.lang.Object minR) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setObjectValue(minR);
        }
    }

    /**
     * Sets (as xml) the "minR" attribute
     */
    @Override
    public void xsetMinR(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate minR) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(minR);
        }
    }

    /**
     * Unsets the "minR" attribute
     */
    @Override
    public void unsetMinR() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "maxR" attribute
     */
    @Override
    public java.lang.Object getMaxR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "maxR" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetMaxR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "maxR" attribute
     */
    @Override
    public boolean isSetMaxR() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "maxR" attribute
     */
    @Override
    public void setMaxR(java.lang.Object maxR) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setObjectValue(maxR);
        }
    }

    /**
     * Sets (as xml) the "maxR" attribute
     */
    @Override
    public void xsetMaxR(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate maxR) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(maxR);
        }
    }

    /**
     * Unsets the "maxR" attribute
     */
    @Override
    public void unsetMaxR() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "gdRefAng" attribute
     */
    @Override
    public java.lang.String getGdRefAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "gdRefAng" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName xgetGdRefAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "gdRefAng" attribute
     */
    @Override
    public boolean isSetGdRefAng() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "gdRefAng" attribute
     */
    @Override
    public void setGdRefAng(java.lang.String gdRefAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setStringValue(gdRefAng);
        }
    }

    /**
     * Sets (as xml) the "gdRefAng" attribute
     */
    @Override
    public void xsetGdRefAng(org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName gdRefAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(gdRefAng);
        }
    }

    /**
     * Unsets the "gdRefAng" attribute
     */
    @Override
    public void unsetGdRefAng() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "minAng" attribute
     */
    @Override
    public java.lang.Object getMinAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "minAng" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle xgetMinAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "minAng" attribute
     */
    @Override
    public boolean isSetMinAng() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "minAng" attribute
     */
    @Override
    public void setMinAng(java.lang.Object minAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setObjectValue(minAng);
        }
    }

    /**
     * Sets (as xml) the "minAng" attribute
     */
    @Override
    public void xsetMinAng(org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle minAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(minAng);
        }
    }

    /**
     * Unsets the "minAng" attribute
     */
    @Override
    public void unsetMinAng() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "maxAng" attribute
     */
    @Override
    public java.lang.Object getMaxAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "maxAng" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle xgetMaxAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "maxAng" attribute
     */
    @Override
    public boolean isSetMaxAng() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "maxAng" attribute
     */
    @Override
    public void setMaxAng(java.lang.Object maxAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setObjectValue(maxAng);
        }
    }

    /**
     * Sets (as xml) the "maxAng" attribute
     */
    @Override
    public void xsetMaxAng(org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle maxAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(maxAng);
        }
    }

    /**
     * Unsets the "maxAng" attribute
     */
    @Override
    public void unsetMaxAng() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }
}
