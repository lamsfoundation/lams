/*
 * XML Type:  CT_PhoneticPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PhoneticPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTPhoneticPrImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr {
    private static final long serialVersionUID = 1L;

    public CTPhoneticPrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "fontId"),
        new QName("", "type"),
        new QName("", "alignment"),
    };


    /**
     * Gets the "fontId" attribute
     */
    @Override
    public long getFontId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "fontId" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontId xgetFontId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontId target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontId)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "fontId" attribute
     */
    @Override
    public void setFontId(long fontId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setLongValue(fontId);
        }
    }

    /**
     * Sets (as xml) the "fontId" attribute
     */
    @Override
    public void xsetFontId(org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontId fontId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontId target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontId)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontId)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(fontId);
        }
    }

    /**
     * Gets the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType.Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType xgetType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "type" attribute
     */
    @Override
    public boolean isSetType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "type" attribute
     */
    @Override
    public void setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType.Enum type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(type);
        }
    }

    /**
     * Sets (as xml) the "type" attribute
     */
    @Override
    public void xsetType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType type) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(type);
        }
    }

    /**
     * Unsets the "type" attribute
     */
    @Override
    public void unsetType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "alignment" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment.Enum getAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "alignment" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment xgetAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "alignment" attribute
     */
    @Override
    public boolean isSetAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "alignment" attribute
     */
    @Override
    public void setAlignment(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment.Enum alignment) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(alignment);
        }
    }

    /**
     * Sets (as xml) the "alignment" attribute
     */
    @Override
    public void xsetAlignment(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment alignment) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(alignment);
        }
    }

    /**
     * Unsets the "alignment" attribute
     */
    @Override
    public void unsetAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}
