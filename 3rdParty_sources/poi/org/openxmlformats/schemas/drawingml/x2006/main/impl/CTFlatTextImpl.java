/*
 * XML Type:  CT_FlatText
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FlatText(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFlatTextImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText {
    private static final long serialVersionUID = 1L;

    public CTFlatTextImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "z"),
    };


    /**
     * Gets the "z" attribute
     */
    @Override
    public java.lang.Object getZ() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "z" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetZ() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "z" attribute
     */
    @Override
    public boolean isSetZ() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "z" attribute
     */
    @Override
    public void setZ(java.lang.Object z) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(z);
        }
    }

    /**
     * Sets (as xml) the "z" attribute
     */
    @Override
    public void xsetZ(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate z) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(z);
        }
    }

    /**
     * Unsets the "z" attribute
     */
    @Override
    public void unsetZ() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }
}
