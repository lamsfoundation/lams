/*
 * XML Type:  CT_PaperSource
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPaperSource
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PaperSource(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPaperSourceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPaperSource {
    private static final long serialVersionUID = 1L;

    public CTPaperSourceImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "first"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "other"),
    };


    /**
     * Gets the "first" attribute
     */
    @Override
    public java.math.BigInteger getFirst() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "first" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetFirst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "first" attribute
     */
    @Override
    public boolean isSetFirst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "first" attribute
     */
    @Override
    public void setFirst(java.math.BigInteger first) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setBigIntegerValue(first);
        }
    }

    /**
     * Sets (as xml) the "first" attribute
     */
    @Override
    public void xsetFirst(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber first) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(first);
        }
    }

    /**
     * Unsets the "first" attribute
     */
    @Override
    public void unsetFirst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "other" attribute
     */
    @Override
    public java.math.BigInteger getOther() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "other" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetOther() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "other" attribute
     */
    @Override
    public boolean isSetOther() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "other" attribute
     */
    @Override
    public void setOther(java.math.BigInteger other) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setBigIntegerValue(other);
        }
    }

    /**
     * Sets (as xml) the "other" attribute
     */
    @Override
    public void xsetOther(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber other) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(other);
        }
    }

    /**
     * Unsets the "other" attribute
     */
    @Override
    public void unsetOther() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}
