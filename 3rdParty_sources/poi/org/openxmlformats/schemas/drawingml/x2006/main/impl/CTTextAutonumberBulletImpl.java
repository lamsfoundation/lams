/*
 * XML Type:  CT_TextAutonumberBullet
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TextAutonumberBullet(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTextAutonumberBulletImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet {
    private static final long serialVersionUID = 1L;

    public CTTextAutonumberBulletImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "type"),
        new QName("", "startAt"),
    };


    /**
     * Gets the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme.Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme xgetType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "type" attribute
     */
    @Override
    public void setType(org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme.Enum type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(type);
        }
    }

    /**
     * Sets (as xml) the "type" attribute
     */
    @Override
    public void xsetType(org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme type) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(type);
        }
    }

    /**
     * Gets the "startAt" attribute
     */
    @Override
    public int getStartAt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "startAt" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextBulletStartAtNum xgetStartAt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextBulletStartAtNum target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextBulletStartAtNum)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextBulletStartAtNum)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "startAt" attribute
     */
    @Override
    public boolean isSetStartAt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "startAt" attribute
     */
    @Override
    public void setStartAt(int startAt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setIntValue(startAt);
        }
    }

    /**
     * Sets (as xml) the "startAt" attribute
     */
    @Override
    public void xsetStartAt(org.openxmlformats.schemas.drawingml.x2006.main.STTextBulletStartAtNum startAt) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextBulletStartAtNum target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextBulletStartAtNum)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextBulletStartAtNum)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(startAt);
        }
    }

    /**
     * Unsets the "startAt" attribute
     */
    @Override
    public void unsetStartAt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}
