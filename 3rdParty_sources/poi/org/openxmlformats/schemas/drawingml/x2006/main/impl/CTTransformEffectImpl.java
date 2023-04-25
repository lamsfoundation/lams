/*
 * XML Type:  CT_TransformEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTransformEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TransformEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTransformEffectImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTransformEffect {
    private static final long serialVersionUID = 1L;

    public CTTransformEffectImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "sx"),
        new QName("", "sy"),
        new QName("", "kx"),
        new QName("", "ky"),
        new QName("", "tx"),
        new QName("", "ty"),
    };


    /**
     * Gets the "sx" attribute
     */
    @Override
    public java.lang.Object getSx() {
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
     * Gets (as xml) the "sx" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetSx() {
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
     * True if has "sx" attribute
     */
    @Override
    public boolean isSetSx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "sx" attribute
     */
    @Override
    public void setSx(java.lang.Object sx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(sx);
        }
    }

    /**
     * Sets (as xml) the "sx" attribute
     */
    @Override
    public void xsetSx(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage sx) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(sx);
        }
    }

    /**
     * Unsets the "sx" attribute
     */
    @Override
    public void unsetSx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "sy" attribute
     */
    @Override
    public java.lang.Object getSy() {
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
     * Gets (as xml) the "sy" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetSy() {
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
     * True if has "sy" attribute
     */
    @Override
    public boolean isSetSy() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "sy" attribute
     */
    @Override
    public void setSy(java.lang.Object sy) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(sy);
        }
    }

    /**
     * Sets (as xml) the "sy" attribute
     */
    @Override
    public void xsetSy(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage sy) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(sy);
        }
    }

    /**
     * Unsets the "sy" attribute
     */
    @Override
    public void unsetSy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "kx" attribute
     */
    @Override
    public int getKx() {
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
     * Gets (as xml) the "kx" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle xgetKx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "kx" attribute
     */
    @Override
    public boolean isSetKx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "kx" attribute
     */
    @Override
    public void setKx(int kx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setIntValue(kx);
        }
    }

    /**
     * Sets (as xml) the "kx" attribute
     */
    @Override
    public void xsetKx(org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle kx) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(kx);
        }
    }

    /**
     * Unsets the "kx" attribute
     */
    @Override
    public void unsetKx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "ky" attribute
     */
    @Override
    public int getKy() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "ky" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle xgetKy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "ky" attribute
     */
    @Override
    public boolean isSetKy() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "ky" attribute
     */
    @Override
    public void setKy(int ky) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setIntValue(ky);
        }
    }

    /**
     * Sets (as xml) the "ky" attribute
     */
    @Override
    public void xsetKy(org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle ky) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(ky);
        }
    }

    /**
     * Unsets the "ky" attribute
     */
    @Override
    public void unsetKy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "tx" attribute
     */
    @Override
    public java.lang.Object getTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "tx" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_default_attribute_value(PROPERTY_QNAME[4]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setObjectValue(tx);
        }
    }

    /**
     * Sets (as xml) the "tx" attribute
     */
    @Override
    public void xsetTx(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate tx) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[4]);
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
            get_store().remove_attribute(PROPERTY_QNAME[4]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "ty" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetTy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_default_attribute_value(PROPERTY_QNAME[5]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setObjectValue(ty);
        }
    }

    /**
     * Sets (as xml) the "ty" attribute
     */
    @Override
    public void xsetTy(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate ty) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[5]);
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
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }
}
