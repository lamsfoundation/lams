/*
 * XML Type:  SignaturePolicyIdentifierType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SignaturePolicyIdentifierType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class SignaturePolicyIdentifierTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType {
    private static final long serialVersionUID = 1L;

    public SignaturePolicyIdentifierTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignaturePolicyId"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignaturePolicyImplied"),
    };


    /**
     * Gets the "SignaturePolicyId" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignaturePolicyIdType getSignaturePolicyId() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignaturePolicyIdType target = null;
            target = (org.etsi.uri.x01903.v13.SignaturePolicyIdType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "SignaturePolicyId" element
     */
    @Override
    public boolean isSetSignaturePolicyId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "SignaturePolicyId" element
     */
    @Override
    public void setSignaturePolicyId(org.etsi.uri.x01903.v13.SignaturePolicyIdType signaturePolicyId) {
        generatedSetterHelperImpl(signaturePolicyId, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignaturePolicyId" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignaturePolicyIdType addNewSignaturePolicyId() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignaturePolicyIdType target = null;
            target = (org.etsi.uri.x01903.v13.SignaturePolicyIdType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "SignaturePolicyId" element
     */
    @Override
    public void unsetSignaturePolicyId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "SignaturePolicyImplied" element
     */
    @Override
    public org.apache.xmlbeans.XmlObject getSignaturePolicyImplied() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "SignaturePolicyImplied" element
     */
    @Override
    public boolean isSetSignaturePolicyImplied() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "SignaturePolicyImplied" element
     */
    @Override
    public void setSignaturePolicyImplied(org.apache.xmlbeans.XmlObject signaturePolicyImplied) {
        generatedSetterHelperImpl(signaturePolicyImplied, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignaturePolicyImplied" element
     */
    @Override
    public org.apache.xmlbeans.XmlObject addNewSignaturePolicyImplied() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "SignaturePolicyImplied" element
     */
    @Override
    public void unsetSignaturePolicyImplied() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
