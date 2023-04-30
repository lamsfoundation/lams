/*
 * XML Type:  SignedPropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignedPropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SignedPropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class SignedPropertiesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SignedPropertiesType {
    private static final long serialVersionUID = 1L;

    public SignedPropertiesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignedSignatureProperties"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignedDataObjectProperties"),
        new QName("", "Id"),
    };


    /**
     * Gets the "SignedSignatureProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignedSignaturePropertiesType getSignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignedSignaturePropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.SignedSignaturePropertiesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "SignedSignatureProperties" element
     */
    @Override
    public boolean isSetSignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "SignedSignatureProperties" element
     */
    @Override
    public void setSignedSignatureProperties(org.etsi.uri.x01903.v13.SignedSignaturePropertiesType signedSignatureProperties) {
        generatedSetterHelperImpl(signedSignatureProperties, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignedSignatureProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignedSignaturePropertiesType addNewSignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignedSignaturePropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.SignedSignaturePropertiesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "SignedSignatureProperties" element
     */
    @Override
    public void unsetSignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "SignedDataObjectProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType getSignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "SignedDataObjectProperties" element
     */
    @Override
    public boolean isSetSignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "SignedDataObjectProperties" element
     */
    @Override
    public void setSignedDataObjectProperties(org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType signedDataObjectProperties) {
        generatedSetterHelperImpl(signedDataObjectProperties, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignedDataObjectProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType addNewSignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "SignedDataObjectProperties" element
     */
    @Override
    public void unsetSignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "Id" attribute
     */
    @Override
    public boolean isSetId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(id);
        }
    }

    /**
     * Unsets the "Id" attribute
     */
    @Override
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}
