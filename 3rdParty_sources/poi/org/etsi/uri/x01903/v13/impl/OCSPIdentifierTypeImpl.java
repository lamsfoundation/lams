/*
 * XML Type:  OCSPIdentifierType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OCSPIdentifierType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML OCSPIdentifierType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class OCSPIdentifierTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.OCSPIdentifierType {
    private static final long serialVersionUID = 1L;

    public OCSPIdentifierTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "ResponderID"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "ProducedAt"),
        new QName("", "URI"),
    };


    /**
     * Gets the "ResponderID" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ResponderIDType getResponderID() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ResponderIDType target = null;
            target = (org.etsi.uri.x01903.v13.ResponderIDType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "ResponderID" element
     */
    @Override
    public void setResponderID(org.etsi.uri.x01903.v13.ResponderIDType responderID) {
        generatedSetterHelperImpl(responderID, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ResponderID" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ResponderIDType addNewResponderID() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ResponderIDType target = null;
            target = (org.etsi.uri.x01903.v13.ResponderIDType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "ProducedAt" element
     */
    @Override
    public java.util.Calendar getProducedAt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getCalendarValue();
        }
    }

    /**
     * Gets (as xml) the "ProducedAt" element
     */
    @Override
    public org.apache.xmlbeans.XmlDateTime xgetProducedAt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * Sets the "ProducedAt" element
     */
    @Override
    public void setProducedAt(java.util.Calendar producedAt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setCalendarValue(producedAt);
        }
    }

    /**
     * Sets (as xml) the "ProducedAt" element
     */
    @Override
    public void xsetProducedAt(org.apache.xmlbeans.XmlDateTime producedAt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDateTime)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(producedAt);
        }
    }

    /**
     * Gets the "URI" attribute
     */
    @Override
    public java.lang.String getURI() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "URI" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI xgetURI() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "URI" attribute
     */
    @Override
    public boolean isSetURI() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "URI" attribute
     */
    @Override
    public void setURI(java.lang.String uri) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(uri);
        }
    }

    /**
     * Sets (as xml) the "URI" attribute
     */
    @Override
    public void xsetURI(org.apache.xmlbeans.XmlAnyURI uri) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(uri);
        }
    }

    /**
     * Unsets the "URI" attribute
     */
    @Override
    public void unsetURI() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}
