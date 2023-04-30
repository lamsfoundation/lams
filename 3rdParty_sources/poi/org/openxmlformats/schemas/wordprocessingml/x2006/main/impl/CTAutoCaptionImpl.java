/*
 * XML Type:  CT_AutoCaption
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AutoCaption(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTAutoCaptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption {
    private static final long serialVersionUID = 1L;

    public CTAutoCaptionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "name"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "caption"),
    };


    /**
     * Gets the "name" attribute
     */
    @Override
    public java.lang.String getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "name" attribute
     */
    @Override
    public void setName(java.lang.String name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" attribute
     */
    @Override
    public void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString name) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(name);
        }
    }

    /**
     * Gets the "caption" attribute
     */
    @Override
    public java.lang.String getCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "caption" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "caption" attribute
     */
    @Override
    public void setCaption(java.lang.String caption) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(caption);
        }
    }

    /**
     * Sets (as xml) the "caption" attribute
     */
    @Override
    public void xsetCaption(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString caption) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(caption);
        }
    }
}
