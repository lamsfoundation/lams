/*
 * XML Type:  CT_Border
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Border(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTBorderImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder {
    private static final long serialVersionUID = 1L;

    public CTBorderImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "val"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "color"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "themeColor"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "themeTint"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "themeShade"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sz"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "space"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "shadow"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "frame"),
    };


    /**
     * Gets the "val" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "val" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder xgetVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "val" attribute
     */
    @Override
    public void setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder.Enum val) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(val);
        }
    }

    /**
     * Sets (as xml) the "val" attribute
     */
    @Override
    public void xsetVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder val) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(val);
        }
    }

    /**
     * Gets the "color" attribute
     */
    @Override
    public java.lang.Object getColor() {
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
     * Gets (as xml) the "color" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor xgetColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "color" attribute
     */
    @Override
    public boolean isSetColor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "color" attribute
     */
    @Override
    public void setColor(java.lang.Object color) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(color);
        }
    }

    /**
     * Sets (as xml) the "color" attribute
     */
    @Override
    public void xsetColor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor color) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(color);
        }
    }

    /**
     * Unsets the "color" attribute
     */
    @Override
    public void unsetColor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "themeColor" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor.Enum getThemeColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "themeColor" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor xgetThemeColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "themeColor" attribute
     */
    @Override
    public boolean isSetThemeColor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "themeColor" attribute
     */
    @Override
    public void setThemeColor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor.Enum themeColor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(themeColor);
        }
    }

    /**
     * Sets (as xml) the "themeColor" attribute
     */
    @Override
    public void xsetThemeColor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor themeColor) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(themeColor);
        }
    }

    /**
     * Unsets the "themeColor" attribute
     */
    @Override
    public void unsetThemeColor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "themeTint" attribute
     */
    @Override
    public byte[] getThemeTint() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "themeTint" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber xgetThemeTint() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "themeTint" attribute
     */
    @Override
    public boolean isSetThemeTint() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "themeTint" attribute
     */
    @Override
    public void setThemeTint(byte[] themeTint) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setByteArrayValue(themeTint);
        }
    }

    /**
     * Sets (as xml) the "themeTint" attribute
     */
    @Override
    public void xsetThemeTint(org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber themeTint) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(themeTint);
        }
    }

    /**
     * Unsets the "themeTint" attribute
     */
    @Override
    public void unsetThemeTint() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "themeShade" attribute
     */
    @Override
    public byte[] getThemeShade() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "themeShade" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber xgetThemeShade() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "themeShade" attribute
     */
    @Override
    public boolean isSetThemeShade() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "themeShade" attribute
     */
    @Override
    public void setThemeShade(byte[] themeShade) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setByteArrayValue(themeShade);
        }
    }

    /**
     * Sets (as xml) the "themeShade" attribute
     */
    @Override
    public void xsetThemeShade(org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber themeShade) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(themeShade);
        }
    }

    /**
     * Unsets the "themeShade" attribute
     */
    @Override
    public void unsetThemeShade() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "sz" attribute
     */
    @Override
    public java.math.BigInteger getSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "sz" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STEighthPointMeasure xgetSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STEighthPointMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STEighthPointMeasure)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "sz" attribute
     */
    @Override
    public boolean isSetSz() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "sz" attribute
     */
    @Override
    public void setSz(java.math.BigInteger sz) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setBigIntegerValue(sz);
        }
    }

    /**
     * Sets (as xml) the "sz" attribute
     */
    @Override
    public void xsetSz(org.openxmlformats.schemas.wordprocessingml.x2006.main.STEighthPointMeasure sz) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STEighthPointMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STEighthPointMeasure)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STEighthPointMeasure)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(sz);
        }
    }

    /**
     * Unsets the "sz" attribute
     */
    @Override
    public void unsetSz() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "space" attribute
     */
    @Override
    public java.math.BigInteger getSpace() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "space" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STPointMeasure xgetSpace() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STPointMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPointMeasure)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPointMeasure)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return target;
        }
    }

    /**
     * True if has "space" attribute
     */
    @Override
    public boolean isSetSpace() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "space" attribute
     */
    @Override
    public void setSpace(java.math.BigInteger space) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setBigIntegerValue(space);
        }
    }

    /**
     * Sets (as xml) the "space" attribute
     */
    @Override
    public void xsetSpace(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPointMeasure space) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STPointMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPointMeasure)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STPointMeasure)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(space);
        }
    }

    /**
     * Unsets the "space" attribute
     */
    @Override
    public void unsetSpace() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "shadow" attribute
     */
    @Override
    public java.lang.Object getShadow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "shadow" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetShadow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "shadow" attribute
     */
    @Override
    public boolean isSetShadow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "shadow" attribute
     */
    @Override
    public void setShadow(java.lang.Object shadow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setObjectValue(shadow);
        }
    }

    /**
     * Sets (as xml) the "shadow" attribute
     */
    @Override
    public void xsetShadow(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff shadow) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(shadow);
        }
    }

    /**
     * Unsets the "shadow" attribute
     */
    @Override
    public void unsetShadow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "frame" attribute
     */
    @Override
    public java.lang.Object getFrame() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "frame" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetFrame() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "frame" attribute
     */
    @Override
    public boolean isSetFrame() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "frame" attribute
     */
    @Override
    public void setFrame(java.lang.Object frame) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setObjectValue(frame);
        }
    }

    /**
     * Sets (as xml) the "frame" attribute
     */
    @Override
    public void xsetFrame(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff frame) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(frame);
        }
    }

    /**
     * Unsets the "frame" attribute
     */
    @Override
    public void unsetFrame() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }
}
