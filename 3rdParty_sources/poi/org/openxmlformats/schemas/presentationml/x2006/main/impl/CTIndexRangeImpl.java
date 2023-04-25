/*
 * XML Type:  CT_IndexRange
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_IndexRange(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTIndexRangeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange {
    private static final long serialVersionUID = 1L;

    public CTIndexRangeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "st"),
        new QName("", "end"),
    };


    /**
     * Gets the "st" attribute
     */
    @Override
    public long getSt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "st" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STIndex xgetSt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STIndex target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STIndex)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "st" attribute
     */
    @Override
    public void setSt(long st) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setLongValue(st);
        }
    }

    /**
     * Sets (as xml) the "st" attribute
     */
    @Override
    public void xsetSt(org.openxmlformats.schemas.presentationml.x2006.main.STIndex st) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STIndex target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STIndex)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STIndex)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(st);
        }
    }

    /**
     * Gets the "end" attribute
     */
    @Override
    public long getEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "end" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STIndex xgetEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STIndex target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STIndex)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "end" attribute
     */
    @Override
    public void setEnd(long end) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(end);
        }
    }

    /**
     * Sets (as xml) the "end" attribute
     */
    @Override
    public void xsetEnd(org.openxmlformats.schemas.presentationml.x2006.main.STIndex end) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STIndex target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STIndex)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STIndex)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(end);
        }
    }
}
