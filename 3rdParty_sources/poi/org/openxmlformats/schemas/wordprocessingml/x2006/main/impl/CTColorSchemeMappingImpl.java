/*
 * XML Type:  CT_ColorSchemeMapping
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColorSchemeMapping
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ColorSchemeMapping(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTColorSchemeMappingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColorSchemeMapping {
    private static final long serialVersionUID = 1L;

    public CTColorSchemeMappingImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bg1"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "t1"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bg2"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "t2"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "accent1"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "accent2"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "accent3"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "accent4"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "accent5"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "accent6"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hyperlink"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "followedHyperlink"),
    };


    /**
     * Gets the "bg1" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum getBg1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "bg1" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex xgetBg1() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "bg1" attribute
     */
    @Override
    public boolean isSetBg1() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "bg1" attribute
     */
    @Override
    public void setBg1(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum bg1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(bg1);
        }
    }

    /**
     * Sets (as xml) the "bg1" attribute
     */
    @Override
    public void xsetBg1(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex bg1) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(bg1);
        }
    }

    /**
     * Unsets the "bg1" attribute
     */
    @Override
    public void unsetBg1() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "t1" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum getT1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "t1" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex xgetT1() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "t1" attribute
     */
    @Override
    public boolean isSetT1() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "t1" attribute
     */
    @Override
    public void setT1(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum t1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(t1);
        }
    }

    /**
     * Sets (as xml) the "t1" attribute
     */
    @Override
    public void xsetT1(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex t1) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(t1);
        }
    }

    /**
     * Unsets the "t1" attribute
     */
    @Override
    public void unsetT1() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "bg2" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum getBg2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "bg2" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex xgetBg2() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "bg2" attribute
     */
    @Override
    public boolean isSetBg2() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "bg2" attribute
     */
    @Override
    public void setBg2(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum bg2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(bg2);
        }
    }

    /**
     * Sets (as xml) the "bg2" attribute
     */
    @Override
    public void xsetBg2(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex bg2) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(bg2);
        }
    }

    /**
     * Unsets the "bg2" attribute
     */
    @Override
    public void unsetBg2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "t2" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum getT2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "t2" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex xgetT2() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "t2" attribute
     */
    @Override
    public boolean isSetT2() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "t2" attribute
     */
    @Override
    public void setT2(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum t2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(t2);
        }
    }

    /**
     * Sets (as xml) the "t2" attribute
     */
    @Override
    public void xsetT2(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex t2) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(t2);
        }
    }

    /**
     * Unsets the "t2" attribute
     */
    @Override
    public void unsetT2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "accent1" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum getAccent1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accent1" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex xgetAccent1() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "accent1" attribute
     */
    @Override
    public boolean isSetAccent1() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "accent1" attribute
     */
    @Override
    public void setAccent1(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum accent1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(accent1);
        }
    }

    /**
     * Sets (as xml) the "accent1" attribute
     */
    @Override
    public void xsetAccent1(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex accent1) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(accent1);
        }
    }

    /**
     * Unsets the "accent1" attribute
     */
    @Override
    public void unsetAccent1() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "accent2" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum getAccent2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accent2" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex xgetAccent2() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "accent2" attribute
     */
    @Override
    public boolean isSetAccent2() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "accent2" attribute
     */
    @Override
    public void setAccent2(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum accent2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(accent2);
        }
    }

    /**
     * Sets (as xml) the "accent2" attribute
     */
    @Override
    public void xsetAccent2(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex accent2) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(accent2);
        }
    }

    /**
     * Unsets the "accent2" attribute
     */
    @Override
    public void unsetAccent2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "accent3" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum getAccent3() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accent3" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex xgetAccent3() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "accent3" attribute
     */
    @Override
    public boolean isSetAccent3() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "accent3" attribute
     */
    @Override
    public void setAccent3(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum accent3) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(accent3);
        }
    }

    /**
     * Sets (as xml) the "accent3" attribute
     */
    @Override
    public void xsetAccent3(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex accent3) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(accent3);
        }
    }

    /**
     * Unsets the "accent3" attribute
     */
    @Override
    public void unsetAccent3() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "accent4" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum getAccent4() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accent4" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex xgetAccent4() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "accent4" attribute
     */
    @Override
    public boolean isSetAccent4() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "accent4" attribute
     */
    @Override
    public void setAccent4(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum accent4) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setEnumValue(accent4);
        }
    }

    /**
     * Sets (as xml) the "accent4" attribute
     */
    @Override
    public void xsetAccent4(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex accent4) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(accent4);
        }
    }

    /**
     * Unsets the "accent4" attribute
     */
    @Override
    public void unsetAccent4() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "accent5" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum getAccent5() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accent5" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex xgetAccent5() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "accent5" attribute
     */
    @Override
    public boolean isSetAccent5() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "accent5" attribute
     */
    @Override
    public void setAccent5(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum accent5) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setEnumValue(accent5);
        }
    }

    /**
     * Sets (as xml) the "accent5" attribute
     */
    @Override
    public void xsetAccent5(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex accent5) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(accent5);
        }
    }

    /**
     * Unsets the "accent5" attribute
     */
    @Override
    public void unsetAccent5() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "accent6" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum getAccent6() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accent6" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex xgetAccent6() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "accent6" attribute
     */
    @Override
    public boolean isSetAccent6() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "accent6" attribute
     */
    @Override
    public void setAccent6(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum accent6) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setEnumValue(accent6);
        }
    }

    /**
     * Sets (as xml) the "accent6" attribute
     */
    @Override
    public void xsetAccent6(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex accent6) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(accent6);
        }
    }

    /**
     * Unsets the "accent6" attribute
     */
    @Override
    public void unsetAccent6() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "hyperlink" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum getHyperlink() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "hyperlink" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex xgetHyperlink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * True if has "hyperlink" attribute
     */
    @Override
    public boolean isSetHyperlink() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "hyperlink" attribute
     */
    @Override
    public void setHyperlink(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum hyperlink) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setEnumValue(hyperlink);
        }
    }

    /**
     * Sets (as xml) the "hyperlink" attribute
     */
    @Override
    public void xsetHyperlink(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex hyperlink) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(hyperlink);
        }
    }

    /**
     * Unsets the "hyperlink" attribute
     */
    @Override
    public void unsetHyperlink() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "followedHyperlink" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum getFollowedHyperlink() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "followedHyperlink" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex xgetFollowedHyperlink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * True if has "followedHyperlink" attribute
     */
    @Override
    public boolean isSetFollowedHyperlink() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "followedHyperlink" attribute
     */
    @Override
    public void setFollowedHyperlink(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.Enum followedHyperlink) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setEnumValue(followedHyperlink);
        }
    }

    /**
     * Sets (as xml) the "followedHyperlink" attribute
     */
    @Override
    public void xsetFollowedHyperlink(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex followedHyperlink) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(followedHyperlink);
        }
    }

    /**
     * Unsets the "followedHyperlink" attribute
     */
    @Override
    public void unsetFollowedHyperlink() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }
}
