/*
 * XML Type:  CRLRefType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CRLRefType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CRLRefType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class CRLRefTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CRLRefType {
    private static final long serialVersionUID = 1L;

    public CRLRefTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "DigestAlgAndValue"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CRLIdentifier"),
    };


    /**
     * Gets the "DigestAlgAndValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DigestAlgAndValueType getDigestAlgAndValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DigestAlgAndValueType target = null;
            target = (org.etsi.uri.x01903.v13.DigestAlgAndValueType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "DigestAlgAndValue" element
     */
    @Override
    public void setDigestAlgAndValue(org.etsi.uri.x01903.v13.DigestAlgAndValueType digestAlgAndValue) {
        generatedSetterHelperImpl(digestAlgAndValue, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "DigestAlgAndValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DigestAlgAndValueType addNewDigestAlgAndValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DigestAlgAndValueType target = null;
            target = (org.etsi.uri.x01903.v13.DigestAlgAndValueType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "CRLIdentifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CRLIdentifierType getCRLIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CRLIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.CRLIdentifierType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "CRLIdentifier" element
     */
    @Override
    public boolean isSetCRLIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "CRLIdentifier" element
     */
    @Override
    public void setCRLIdentifier(org.etsi.uri.x01903.v13.CRLIdentifierType crlIdentifier) {
        generatedSetterHelperImpl(crlIdentifier, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CRLIdentifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CRLIdentifierType addNewCRLIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CRLIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.CRLIdentifierType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "CRLIdentifier" element
     */
    @Override
    public void unsetCRLIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
