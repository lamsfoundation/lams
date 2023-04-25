/*
 * XML Type:  CT_Fonts
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Fonts(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFontsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts {
    private static final long serialVersionUID = 1L;

    public CTFontsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hint"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ascii"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hAnsi"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "eastAsia"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "cs"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "asciiTheme"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hAnsiTheme"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "eastAsiaTheme"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "cstheme"),
    };


    /**
     * Gets the "hint" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STHint.Enum getHint() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHint.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "hint" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STHint xgetHint() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STHint target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHint)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "hint" attribute
     */
    @Override
    public boolean isSetHint() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "hint" attribute
     */
    @Override
    public void setHint(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHint.Enum hint) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(hint);
        }
    }

    /**
     * Sets (as xml) the "hint" attribute
     */
    @Override
    public void xsetHint(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHint hint) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STHint target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHint)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHint)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(hint);
        }
    }

    /**
     * Unsets the "hint" attribute
     */
    @Override
    public void unsetHint() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "ascii" attribute
     */
    @Override
    public java.lang.String getAscii() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "ascii" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetAscii() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "ascii" attribute
     */
    @Override
    public boolean isSetAscii() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "ascii" attribute
     */
    @Override
    public void setAscii(java.lang.String ascii) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(ascii);
        }
    }

    /**
     * Sets (as xml) the "ascii" attribute
     */
    @Override
    public void xsetAscii(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString ascii) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(ascii);
        }
    }

    /**
     * Unsets the "ascii" attribute
     */
    @Override
    public void unsetAscii() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "hAnsi" attribute
     */
    @Override
    public java.lang.String getHAnsi() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "hAnsi" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetHAnsi() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "hAnsi" attribute
     */
    @Override
    public boolean isSetHAnsi() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "hAnsi" attribute
     */
    @Override
    public void setHAnsi(java.lang.String hAnsi) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(hAnsi);
        }
    }

    /**
     * Sets (as xml) the "hAnsi" attribute
     */
    @Override
    public void xsetHAnsi(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString hAnsi) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(hAnsi);
        }
    }

    /**
     * Unsets the "hAnsi" attribute
     */
    @Override
    public void unsetHAnsi() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "eastAsia" attribute
     */
    @Override
    public java.lang.String getEastAsia() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "eastAsia" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetEastAsia() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "eastAsia" attribute
     */
    @Override
    public boolean isSetEastAsia() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "eastAsia" attribute
     */
    @Override
    public void setEastAsia(java.lang.String eastAsia) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(eastAsia);
        }
    }

    /**
     * Sets (as xml) the "eastAsia" attribute
     */
    @Override
    public void xsetEastAsia(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString eastAsia) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(eastAsia);
        }
    }

    /**
     * Unsets the "eastAsia" attribute
     */
    @Override
    public void unsetEastAsia() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "cs" attribute
     */
    @Override
    public java.lang.String getCs() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "cs" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetCs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "cs" attribute
     */
    @Override
    public boolean isSetCs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "cs" attribute
     */
    @Override
    public void setCs(java.lang.String cs) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setStringValue(cs);
        }
    }

    /**
     * Sets (as xml) the "cs" attribute
     */
    @Override
    public void xsetCs(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString cs) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(cs);
        }
    }

    /**
     * Unsets the "cs" attribute
     */
    @Override
    public void unsetCs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "asciiTheme" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum getAsciiTheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "asciiTheme" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme xgetAsciiTheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "asciiTheme" attribute
     */
    @Override
    public boolean isSetAsciiTheme() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "asciiTheme" attribute
     */
    @Override
    public void setAsciiTheme(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum asciiTheme) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(asciiTheme);
        }
    }

    /**
     * Sets (as xml) the "asciiTheme" attribute
     */
    @Override
    public void xsetAsciiTheme(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme asciiTheme) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(asciiTheme);
        }
    }

    /**
     * Unsets the "asciiTheme" attribute
     */
    @Override
    public void unsetAsciiTheme() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "hAnsiTheme" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum getHAnsiTheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "hAnsiTheme" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme xgetHAnsiTheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "hAnsiTheme" attribute
     */
    @Override
    public boolean isSetHAnsiTheme() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "hAnsiTheme" attribute
     */
    @Override
    public void setHAnsiTheme(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum hAnsiTheme) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(hAnsiTheme);
        }
    }

    /**
     * Sets (as xml) the "hAnsiTheme" attribute
     */
    @Override
    public void xsetHAnsiTheme(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme hAnsiTheme) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(hAnsiTheme);
        }
    }

    /**
     * Unsets the "hAnsiTheme" attribute
     */
    @Override
    public void unsetHAnsiTheme() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "eastAsiaTheme" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum getEastAsiaTheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "eastAsiaTheme" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme xgetEastAsiaTheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "eastAsiaTheme" attribute
     */
    @Override
    public boolean isSetEastAsiaTheme() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "eastAsiaTheme" attribute
     */
    @Override
    public void setEastAsiaTheme(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum eastAsiaTheme) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setEnumValue(eastAsiaTheme);
        }
    }

    /**
     * Sets (as xml) the "eastAsiaTheme" attribute
     */
    @Override
    public void xsetEastAsiaTheme(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme eastAsiaTheme) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(eastAsiaTheme);
        }
    }

    /**
     * Unsets the "eastAsiaTheme" attribute
     */
    @Override
    public void unsetEastAsiaTheme() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "cstheme" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum getCstheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "cstheme" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme xgetCstheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "cstheme" attribute
     */
    @Override
    public boolean isSetCstheme() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "cstheme" attribute
     */
    @Override
    public void setCstheme(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum cstheme) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setEnumValue(cstheme);
        }
    }

    /**
     * Sets (as xml) the "cstheme" attribute
     */
    @Override
    public void xsetCstheme(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme cstheme) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(cstheme);
        }
    }

    /**
     * Unsets the "cstheme" attribute
     */
    @Override
    public void unsetCstheme() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }
}
