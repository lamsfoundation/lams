/*
 * XML Type:  SignaturePolicyIdType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignaturePolicyIdType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SignaturePolicyIdType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class SignaturePolicyIdTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SignaturePolicyIdType {
    private static final long serialVersionUID = 1L;

    public SignaturePolicyIdTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SigPolicyId"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "Transforms"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SigPolicyHash"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SigPolicyQualifiers"),
    };


    /**
     * Gets the "SigPolicyId" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ObjectIdentifierType getSigPolicyId() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ObjectIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.ObjectIdentifierType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SigPolicyId" element
     */
    @Override
    public void setSigPolicyId(org.etsi.uri.x01903.v13.ObjectIdentifierType sigPolicyId) {
        generatedSetterHelperImpl(sigPolicyId, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SigPolicyId" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ObjectIdentifierType addNewSigPolicyId() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ObjectIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.ObjectIdentifierType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "Transforms" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.TransformsType getTransforms() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.TransformsType target = null;
            target = (org.w3.x2000.x09.xmldsig.TransformsType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "Transforms" element
     */
    @Override
    public boolean isSetTransforms() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "Transforms" element
     */
    @Override
    public void setTransforms(org.w3.x2000.x09.xmldsig.TransformsType transforms) {
        generatedSetterHelperImpl(transforms, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Transforms" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.TransformsType addNewTransforms() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.TransformsType target = null;
            target = (org.w3.x2000.x09.xmldsig.TransformsType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "Transforms" element
     */
    @Override
    public void unsetTransforms() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "SigPolicyHash" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DigestAlgAndValueType getSigPolicyHash() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DigestAlgAndValueType target = null;
            target = (org.etsi.uri.x01903.v13.DigestAlgAndValueType)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SigPolicyHash" element
     */
    @Override
    public void setSigPolicyHash(org.etsi.uri.x01903.v13.DigestAlgAndValueType sigPolicyHash) {
        generatedSetterHelperImpl(sigPolicyHash, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SigPolicyHash" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DigestAlgAndValueType addNewSigPolicyHash() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DigestAlgAndValueType target = null;
            target = (org.etsi.uri.x01903.v13.DigestAlgAndValueType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets the "SigPolicyQualifiers" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SigPolicyQualifiersListType getSigPolicyQualifiers() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SigPolicyQualifiersListType target = null;
            target = (org.etsi.uri.x01903.v13.SigPolicyQualifiersListType)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "SigPolicyQualifiers" element
     */
    @Override
    public boolean isSetSigPolicyQualifiers() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "SigPolicyQualifiers" element
     */
    @Override
    public void setSigPolicyQualifiers(org.etsi.uri.x01903.v13.SigPolicyQualifiersListType sigPolicyQualifiers) {
        generatedSetterHelperImpl(sigPolicyQualifiers, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SigPolicyQualifiers" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SigPolicyQualifiersListType addNewSigPolicyQualifiers() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SigPolicyQualifiersListType target = null;
            target = (org.etsi.uri.x01903.v13.SigPolicyQualifiersListType)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "SigPolicyQualifiers" element
     */
    @Override
    public void unsetSigPolicyQualifiers() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}
