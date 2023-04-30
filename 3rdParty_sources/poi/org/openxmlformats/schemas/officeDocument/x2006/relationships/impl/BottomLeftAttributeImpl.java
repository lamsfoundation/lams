/*
 * An XML attribute type.
 * Localname: bottomLeft
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.BottomLeftAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one bottomLeft(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public class BottomLeftAttributeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.relationships.BottomLeftAttribute {
    private static final long serialVersionUID = 1L;

    public BottomLeftAttributeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "bottomLeft"),
    };


    /**
     * Gets the "bottomLeft" attribute
     */
    @Override
    public java.lang.String getBottomLeft() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "bottomLeft" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetBottomLeft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "bottomLeft" attribute
     */
    @Override
    public boolean isSetBottomLeft() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "bottomLeft" attribute
     */
    @Override
    public void setBottomLeft(java.lang.String bottomLeft) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(bottomLeft);
        }
    }

    /**
     * Sets (as xml) the "bottomLeft" attribute
     */
    @Override
    public void xsetBottomLeft(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId bottomLeft) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(bottomLeft);
        }
    }

    /**
     * Unsets the "bottomLeft" attribute
     */
    @Override
    public void unsetBottomLeft() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }
}
