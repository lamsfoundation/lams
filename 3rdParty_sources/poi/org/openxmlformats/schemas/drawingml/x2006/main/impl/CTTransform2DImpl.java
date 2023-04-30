/*
 * XML Type:  CT_Transform2D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Transform2D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTransform2DImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D {
    private static final long serialVersionUID = 1L;

    public CTTransform2DImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "off"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "ext"),
        new QName("", "rot"),
        new QName("", "flipH"),
        new QName("", "flipV"),
    };


    /**
     * Gets the "off" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D getOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "off" element
     */
    @Override
    public boolean isSetOff() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "off" element
     */
    @Override
    public void setOff(org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D off) {
        generatedSetterHelperImpl(off, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "off" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D addNewOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "off" element
     */
    @Override
    public void unsetOff() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "ext" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D getExt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ext" element
     */
    @Override
    public boolean isSetExt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "ext" element
     */
    @Override
    public void setExt(org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D ext) {
        generatedSetterHelperImpl(ext, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ext" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D addNewExt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "ext" element
     */
    @Override
    public void unsetExt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "rot" attribute
     */
    @Override
    public int getRot() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "rot" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAngle xgetRot() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "rot" attribute
     */
    @Override
    public boolean isSetRot() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "rot" attribute
     */
    @Override
    public void setRot(int rot) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setIntValue(rot);
        }
    }

    /**
     * Sets (as xml) the "rot" attribute
     */
    @Override
    public void xsetRot(org.openxmlformats.schemas.drawingml.x2006.main.STAngle rot) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(rot);
        }
    }

    /**
     * Unsets the "rot" attribute
     */
    @Override
    public void unsetRot() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "flipH" attribute
     */
    @Override
    public boolean getFlipH() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "flipH" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFlipH() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "flipH" attribute
     */
    @Override
    public boolean isSetFlipH() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "flipH" attribute
     */
    @Override
    public void setFlipH(boolean flipH) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setBooleanValue(flipH);
        }
    }

    /**
     * Sets (as xml) the "flipH" attribute
     */
    @Override
    public void xsetFlipH(org.apache.xmlbeans.XmlBoolean flipH) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(flipH);
        }
    }

    /**
     * Unsets the "flipH" attribute
     */
    @Override
    public void unsetFlipH() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "flipV" attribute
     */
    @Override
    public boolean getFlipV() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "flipV" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFlipV() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "flipV" attribute
     */
    @Override
    public boolean isSetFlipV() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "flipV" attribute
     */
    @Override
    public void setFlipV(boolean flipV) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setBooleanValue(flipV);
        }
    }

    /**
     * Sets (as xml) the "flipV" attribute
     */
    @Override
    public void xsetFlipV(org.apache.xmlbeans.XmlBoolean flipV) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(flipV);
        }
    }

    /**
     * Unsets the "flipV" attribute
     */
    @Override
    public void unsetFlipV() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}
