/*
 * XML Type:  CT_ObjectLink
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ObjectLink(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTObjectLinkImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTObjectEmbedImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink {
    private static final long serialVersionUID = 1L;

    public CTObjectLinkImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "updateMode"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lockedField"),
    };


    /**
     * Gets the "updateMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode.Enum getUpdateMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "updateMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode xgetUpdateMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "updateMode" attribute
     */
    @Override
    public void setUpdateMode(org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode.Enum updateMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(updateMode);
        }
    }

    /**
     * Sets (as xml) the "updateMode" attribute
     */
    @Override
    public void xsetUpdateMode(org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode updateMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(updateMode);
        }
    }

    /**
     * Gets the "lockedField" attribute
     */
    @Override
    public java.lang.Object getLockedField() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "lockedField" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetLockedField() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "lockedField" attribute
     */
    @Override
    public boolean isSetLockedField() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "lockedField" attribute
     */
    @Override
    public void setLockedField(java.lang.Object lockedField) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(lockedField);
        }
    }

    /**
     * Sets (as xml) the "lockedField" attribute
     */
    @Override
    public void xsetLockedField(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff lockedField) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(lockedField);
        }
    }

    /**
     * Unsets the "lockedField" attribute
     */
    @Override
    public void unsetLockedField() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}
