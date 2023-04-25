/*
 * XML Type:  CT_TextFont
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TextFont(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTextFontImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont {
    private static final long serialVersionUID = 1L;

    public CTTextFontImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "typeface"),
        new QName("", "panose"),
        new QName("", "pitchFamily"),
        new QName("", "charset"),
    };


    /**
     * Gets the "typeface" attribute
     */
    @Override
    public java.lang.String getTypeface() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "typeface" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextTypeface xgetTypeface() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextTypeface target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextTypeface)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "typeface" attribute
     */
    @Override
    public void setTypeface(java.lang.String typeface) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(typeface);
        }
    }

    /**
     * Sets (as xml) the "typeface" attribute
     */
    @Override
    public void xsetTypeface(org.openxmlformats.schemas.drawingml.x2006.main.STTextTypeface typeface) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextTypeface target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextTypeface)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextTypeface)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(typeface);
        }
    }

    /**
     * Gets the "panose" attribute
     */
    @Override
    public byte[] getPanose() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "panose" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPanose xgetPanose() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPanose target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPanose)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "panose" attribute
     */
    @Override
    public boolean isSetPanose() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "panose" attribute
     */
    @Override
    public void setPanose(byte[] panose) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setByteArrayValue(panose);
        }
    }

    /**
     * Sets (as xml) the "panose" attribute
     */
    @Override
    public void xsetPanose(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPanose panose) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPanose target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPanose)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPanose)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(panose);
        }
    }

    /**
     * Unsets the "panose" attribute
     */
    @Override
    public void unsetPanose() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "pitchFamily" attribute
     */
    @Override
    public byte getPitchFamily() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? 0 : target.getByteValue();
        }
    }

    /**
     * Gets (as xml) the "pitchFamily" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPitchFamily xgetPitchFamily() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPitchFamily target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPitchFamily)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPitchFamily)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "pitchFamily" attribute
     */
    @Override
    public boolean isSetPitchFamily() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "pitchFamily" attribute
     */
    @Override
    public void setPitchFamily(byte pitchFamily) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setByteValue(pitchFamily);
        }
    }

    /**
     * Sets (as xml) the "pitchFamily" attribute
     */
    @Override
    public void xsetPitchFamily(org.openxmlformats.schemas.drawingml.x2006.main.STPitchFamily pitchFamily) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPitchFamily target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPitchFamily)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPitchFamily)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(pitchFamily);
        }
    }

    /**
     * Unsets the "pitchFamily" attribute
     */
    @Override
    public void unsetPitchFamily() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "charset" attribute
     */
    @Override
    public byte getCharset() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? 0 : target.getByteValue();
        }
    }

    /**
     * Gets (as xml) the "charset" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlByte xgetCharset() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlByte target = null;
            target = (org.apache.xmlbeans.XmlByte)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlByte)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "charset" attribute
     */
    @Override
    public boolean isSetCharset() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "charset" attribute
     */
    @Override
    public void setCharset(byte charset) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setByteValue(charset);
        }
    }

    /**
     * Sets (as xml) the "charset" attribute
     */
    @Override
    public void xsetCharset(org.apache.xmlbeans.XmlByte charset) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlByte target = null;
            target = (org.apache.xmlbeans.XmlByte)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlByte)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(charset);
        }
    }

    /**
     * Unsets the "charset" attribute
     */
    @Override
    public void unsetCharset() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}
