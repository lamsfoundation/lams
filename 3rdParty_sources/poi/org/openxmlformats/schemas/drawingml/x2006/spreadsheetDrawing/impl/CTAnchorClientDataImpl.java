/*
 * XML Type:  CT_AnchorClientData
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AnchorClientData(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing).
 *
 * This is a complex type.
 */
public class CTAnchorClientDataImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData {
    private static final long serialVersionUID = 1L;

    public CTAnchorClientDataImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "fLocksWithSheet"),
        new QName("", "fPrintsWithSheet"),
    };


    /**
     * Gets the "fLocksWithSheet" attribute
     */
    @Override
    public boolean getFLocksWithSheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "fLocksWithSheet" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFLocksWithSheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "fLocksWithSheet" attribute
     */
    @Override
    public boolean isSetFLocksWithSheet() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "fLocksWithSheet" attribute
     */
    @Override
    public void setFLocksWithSheet(boolean fLocksWithSheet) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setBooleanValue(fLocksWithSheet);
        }
    }

    /**
     * Sets (as xml) the "fLocksWithSheet" attribute
     */
    @Override
    public void xsetFLocksWithSheet(org.apache.xmlbeans.XmlBoolean fLocksWithSheet) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(fLocksWithSheet);
        }
    }

    /**
     * Unsets the "fLocksWithSheet" attribute
     */
    @Override
    public void unsetFLocksWithSheet() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "fPrintsWithSheet" attribute
     */
    @Override
    public boolean getFPrintsWithSheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "fPrintsWithSheet" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFPrintsWithSheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "fPrintsWithSheet" attribute
     */
    @Override
    public boolean isSetFPrintsWithSheet() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "fPrintsWithSheet" attribute
     */
    @Override
    public void setFPrintsWithSheet(boolean fPrintsWithSheet) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setBooleanValue(fPrintsWithSheet);
        }
    }

    /**
     * Sets (as xml) the "fPrintsWithSheet" attribute
     */
    @Override
    public void xsetFPrintsWithSheet(org.apache.xmlbeans.XmlBoolean fPrintsWithSheet) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(fPrintsWithSheet);
        }
    }

    /**
     * Unsets the "fPrintsWithSheet" attribute
     */
    @Override
    public void unsetFPrintsWithSheet() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}
