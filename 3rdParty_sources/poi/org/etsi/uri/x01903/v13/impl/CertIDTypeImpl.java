/*
 * XML Type:  CertIDType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CertIDType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CertIDType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class CertIDTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CertIDType {
    private static final long serialVersionUID = 1L;

    public CertIDTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CertDigest"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "IssuerSerial"),
        new QName("", "URI"),
    };


    /**
     * Gets the "CertDigest" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DigestAlgAndValueType getCertDigest() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DigestAlgAndValueType target = null;
            target = (org.etsi.uri.x01903.v13.DigestAlgAndValueType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "CertDigest" element
     */
    @Override
    public void setCertDigest(org.etsi.uri.x01903.v13.DigestAlgAndValueType certDigest) {
        generatedSetterHelperImpl(certDigest, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CertDigest" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DigestAlgAndValueType addNewCertDigest() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DigestAlgAndValueType target = null;
            target = (org.etsi.uri.x01903.v13.DigestAlgAndValueType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "IssuerSerial" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.X509IssuerSerialType getIssuerSerial() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509IssuerSerialType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509IssuerSerialType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "IssuerSerial" element
     */
    @Override
    public void setIssuerSerial(org.w3.x2000.x09.xmldsig.X509IssuerSerialType issuerSerial) {
        generatedSetterHelperImpl(issuerSerial, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "IssuerSerial" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.X509IssuerSerialType addNewIssuerSerial() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509IssuerSerialType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509IssuerSerialType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
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
