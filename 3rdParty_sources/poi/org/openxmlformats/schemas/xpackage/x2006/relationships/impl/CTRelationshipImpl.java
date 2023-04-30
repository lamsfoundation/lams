/*
 * XML Type:  CT_Relationship
 * Namespace: http://schemas.openxmlformats.org/package/2006/relationships
 * Java type: org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.relationships.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Relationship(@http://schemas.openxmlformats.org/package/2006/relationships).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship.
 */
public class CTRelationshipImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship {
    private static final long serialVersionUID = 1L;

    public CTRelationshipImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType, true);
    }

    protected CTRelationshipImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
        super(sType, b);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "TargetMode"),
        new QName("", "Target"),
        new QName("", "Type"),
        new QName("", "Id"),
    };


    /**
     * Gets the "TargetMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode.Enum getTargetMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : (org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "TargetMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode xgetTargetMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "TargetMode" attribute
     */
    @Override
    public boolean isSetTargetMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "TargetMode" attribute
     */
    @Override
    public void setTargetMode(org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode.Enum targetMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(targetMode);
        }
    }

    /**
     * Sets (as xml) the "TargetMode" attribute
     */
    @Override
    public void xsetTargetMode(org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode targetMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(targetMode);
        }
    }

    /**
     * Unsets the "TargetMode" attribute
     */
    @Override
    public void unsetTargetMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "Target" attribute
     */
    @Override
    public java.lang.String getTarget() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Target" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI xgetTarget() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "Target" attribute
     */
    @Override
    public void setTarget(java.lang.String targetValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(targetValue);
        }
    }

    /**
     * Sets (as xml) the "Target" attribute
     */
    @Override
    public void xsetTarget(org.apache.xmlbeans.XmlAnyURI targetValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(targetValue);
        }
    }

    /**
     * Gets the "Type" attribute
     */
    @Override
    public java.lang.String getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Type" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI xgetType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "Type" attribute
     */
    @Override
    public void setType(java.lang.String type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(type);
        }
    }

    /**
     * Sets (as xml) the "Type" attribute
     */
    @Override
    public void xsetType(org.apache.xmlbeans.XmlAnyURI type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(type);
        }
    }

    /**
     * Gets the "Id" attribute
     */
    @Override
    public java.lang.String getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Id" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlID xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Sets the "Id" attribute
     */
    @Override
    public void setId(java.lang.String id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(id);
        }
    }

    /**
     * Sets (as xml) the "Id" attribute
     */
    @Override
    public void xsetId(org.apache.xmlbeans.XmlID id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(id);
        }
    }
}
