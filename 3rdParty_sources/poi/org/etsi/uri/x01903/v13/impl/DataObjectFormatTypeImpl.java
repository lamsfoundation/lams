/*
 * XML Type:  DataObjectFormatType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.DataObjectFormatType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML DataObjectFormatType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class DataObjectFormatTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.DataObjectFormatType {
    private static final long serialVersionUID = 1L;

    public DataObjectFormatTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "Description"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "ObjectIdentifier"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "MimeType"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "Encoding"),
        new QName("", "ObjectReference"),
    };


    /**
     * Gets the "Description" element
     */
    @Override
    public java.lang.String getDescription() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Description" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetDescription() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * True if has "Description" element
     */
    @Override
    public boolean isSetDescription() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "Description" element
     */
    @Override
    public void setDescription(java.lang.String description) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(description);
        }
    }

    /**
     * Sets (as xml) the "Description" element
     */
    @Override
    public void xsetDescription(org.apache.xmlbeans.XmlString description) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(description);
        }
    }

    /**
     * Unsets the "Description" element
     */
    @Override
    public void unsetDescription() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "ObjectIdentifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ObjectIdentifierType getObjectIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ObjectIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.ObjectIdentifierType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ObjectIdentifier" element
     */
    @Override
    public boolean isSetObjectIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "ObjectIdentifier" element
     */
    @Override
    public void setObjectIdentifier(org.etsi.uri.x01903.v13.ObjectIdentifierType objectIdentifier) {
        generatedSetterHelperImpl(objectIdentifier, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ObjectIdentifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ObjectIdentifierType addNewObjectIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ObjectIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.ObjectIdentifierType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "ObjectIdentifier" element
     */
    @Override
    public void unsetObjectIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "MimeType" element
     */
    @Override
    public java.lang.String getMimeType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "MimeType" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetMimeType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return target;
        }
    }

    /**
     * True if has "MimeType" element
     */
    @Override
    public boolean isSetMimeType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "MimeType" element
     */
    @Override
    public void setMimeType(java.lang.String mimeType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(mimeType);
        }
    }

    /**
     * Sets (as xml) the "MimeType" element
     */
    @Override
    public void xsetMimeType(org.apache.xmlbeans.XmlString mimeType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[2]);
            }
            target.set(mimeType);
        }
    }

    /**
     * Unsets the "MimeType" element
     */
    @Override
    public void unsetMimeType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "Encoding" element
     */
    @Override
    public java.lang.String getEncoding() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Encoding" element
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI xgetEncoding() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return target;
        }
    }

    /**
     * True if has "Encoding" element
     */
    @Override
    public boolean isSetEncoding() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "Encoding" element
     */
    @Override
    public void setEncoding(java.lang.String encoding) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(encoding);
        }
    }

    /**
     * Sets (as xml) the "Encoding" element
     */
    @Override
    public void xsetEncoding(org.apache.xmlbeans.XmlAnyURI encoding) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_element_user(PROPERTY_QNAME[3]);
            }
            target.set(encoding);
        }
    }

    /**
     * Unsets the "Encoding" element
     */
    @Override
    public void unsetEncoding() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "ObjectReference" attribute
     */
    @Override
    public java.lang.String getObjectReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "ObjectReference" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI xgetObjectReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Sets the "ObjectReference" attribute
     */
    @Override
    public void setObjectReference(java.lang.String objectReference) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setStringValue(objectReference);
        }
    }

    /**
     * Sets (as xml) the "ObjectReference" attribute
     */
    @Override
    public void xsetObjectReference(org.apache.xmlbeans.XmlAnyURI objectReference) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(objectReference);
        }
    }
}
