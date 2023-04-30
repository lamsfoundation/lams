/*
 * XML Type:  OCSPRefType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OCSPRefType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML OCSPRefType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class OCSPRefTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.OCSPRefType {
    private static final long serialVersionUID = 1L;

    public OCSPRefTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "OCSPIdentifier"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "DigestAlgAndValue"),
    };


    /**
     * Gets the "OCSPIdentifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OCSPIdentifierType getOCSPIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OCSPIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.OCSPIdentifierType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "OCSPIdentifier" element
     */
    @Override
    public void setOCSPIdentifier(org.etsi.uri.x01903.v13.OCSPIdentifierType ocspIdentifier) {
        generatedSetterHelperImpl(ocspIdentifier, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "OCSPIdentifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OCSPIdentifierType addNewOCSPIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OCSPIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.OCSPIdentifierType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "DigestAlgAndValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DigestAlgAndValueType getDigestAlgAndValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DigestAlgAndValueType target = null;
            target = (org.etsi.uri.x01903.v13.DigestAlgAndValueType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "DigestAlgAndValue" element
     */
    @Override
    public boolean isSetDigestAlgAndValue() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "DigestAlgAndValue" element
     */
    @Override
    public void setDigestAlgAndValue(org.etsi.uri.x01903.v13.DigestAlgAndValueType digestAlgAndValue) {
        generatedSetterHelperImpl(digestAlgAndValue, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "DigestAlgAndValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DigestAlgAndValueType addNewDigestAlgAndValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DigestAlgAndValueType target = null;
            target = (org.etsi.uri.x01903.v13.DigestAlgAndValueType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "DigestAlgAndValue" element
     */
    @Override
    public void unsetDigestAlgAndValue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
