/*
 * XML Type:  CT_Camera
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTCamera
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Camera(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTCameraImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTCamera {
    private static final long serialVersionUID = 1L;

    public CTCameraImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "rot"),
        new QName("", "prst"),
        new QName("", "fov"),
        new QName("", "zoom"),
    };


    /**
     * Gets the "rot" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords getRot() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rot" element
     */
    @Override
    public boolean isSetRot() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "rot" element
     */
    @Override
    public void setRot(org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords rot) {
        generatedSetterHelperImpl(rot, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rot" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords addNewRot() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "rot" element
     */
    @Override
    public void unsetRot() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "prst" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType.Enum getPrst() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "prst" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType xgetPrst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "prst" attribute
     */
    @Override
    public void setPrst(org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType.Enum prst) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(prst);
        }
    }

    /**
     * Sets (as xml) the "prst" attribute
     */
    @Override
    public void xsetPrst(org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType prst) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(prst);
        }
    }

    /**
     * Gets the "fov" attribute
     */
    @Override
    public int getFov() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "fov" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STFOVAngle xgetFov() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFOVAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFOVAngle)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "fov" attribute
     */
    @Override
    public boolean isSetFov() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "fov" attribute
     */
    @Override
    public void setFov(int fov) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setIntValue(fov);
        }
    }

    /**
     * Sets (as xml) the "fov" attribute
     */
    @Override
    public void xsetFov(org.openxmlformats.schemas.drawingml.x2006.main.STFOVAngle fov) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFOVAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFOVAngle)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFOVAngle)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(fov);
        }
    }

    /**
     * Unsets the "fov" attribute
     */
    @Override
    public void unsetFov() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "zoom" attribute
     */
    @Override
    public java.lang.Object getZoom() {
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
     * Gets (as xml) the "zoom" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage xgetZoom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "zoom" attribute
     */
    @Override
    public boolean isSetZoom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "zoom" attribute
     */
    @Override
    public void setZoom(java.lang.Object zoom) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setObjectValue(zoom);
        }
    }

    /**
     * Sets (as xml) the "zoom" attribute
     */
    @Override
    public void xsetZoom(org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage zoom) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(zoom);
        }
    }

    /**
     * Unsets the "zoom" attribute
     */
    @Override
    public void unsetZoom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}
