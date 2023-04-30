/*
 * XML Type:  CT_PageNumber
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageNumber
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PageNumber(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPageNumberImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageNumber {
    private static final long serialVersionUID = 1L;

    public CTPageNumberImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "fmt"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "start"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "chapStyle"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "chapSep"),
    };


    /**
     * Gets the "fmt" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.Enum getFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "fmt" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat xgetFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "fmt" attribute
     */
    @Override
    public boolean isSetFmt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "fmt" attribute
     */
    @Override
    public void setFmt(org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.Enum fmt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(fmt);
        }
    }

    /**
     * Sets (as xml) the "fmt" attribute
     */
    @Override
    public void xsetFmt(org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat fmt) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(fmt);
        }
    }

    /**
     * Unsets the "fmt" attribute
     */
    @Override
    public void unsetFmt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "start" attribute
     */
    @Override
    public java.math.BigInteger getStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "start" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "start" attribute
     */
    @Override
    public boolean isSetStart() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "start" attribute
     */
    @Override
    public void setStart(java.math.BigInteger start) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setBigIntegerValue(start);
        }
    }

    /**
     * Sets (as xml) the "start" attribute
     */
    @Override
    public void xsetStart(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber start) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(start);
        }
    }

    /**
     * Unsets the "start" attribute
     */
    @Override
    public void unsetStart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "chapStyle" attribute
     */
    @Override
    public java.math.BigInteger getChapStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "chapStyle" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetChapStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "chapStyle" attribute
     */
    @Override
    public boolean isSetChapStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "chapStyle" attribute
     */
    @Override
    public void setChapStyle(java.math.BigInteger chapStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBigIntegerValue(chapStyle);
        }
    }

    /**
     * Sets (as xml) the "chapStyle" attribute
     */
    @Override
    public void xsetChapStyle(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber chapStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(chapStyle);
        }
    }

    /**
     * Unsets the "chapStyle" attribute
     */
    @Override
    public void unsetChapStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "chapSep" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep.Enum getChapSep() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "chapSep" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep xgetChapSep() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "chapSep" attribute
     */
    @Override
    public boolean isSetChapSep() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "chapSep" attribute
     */
    @Override
    public void setChapSep(org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep.Enum chapSep) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(chapSep);
        }
    }

    /**
     * Sets (as xml) the "chapSep" attribute
     */
    @Override
    public void xsetChapSep(org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep chapSep) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(chapSep);
        }
    }

    /**
     * Unsets the "chapSep" attribute
     */
    @Override
    public void unsetChapSep() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}
