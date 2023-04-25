/*
 * XML Type:  SignerRoleType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignerRoleType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SignerRoleType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class SignerRoleTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SignerRoleType {
    private static final long serialVersionUID = 1L;

    public SignerRoleTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "ClaimedRoles"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CertifiedRoles"),
    };


    /**
     * Gets the "ClaimedRoles" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ClaimedRolesListType getClaimedRoles() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ClaimedRolesListType target = null;
            target = (org.etsi.uri.x01903.v13.ClaimedRolesListType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ClaimedRoles" element
     */
    @Override
    public boolean isSetClaimedRoles() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "ClaimedRoles" element
     */
    @Override
    public void setClaimedRoles(org.etsi.uri.x01903.v13.ClaimedRolesListType claimedRoles) {
        generatedSetterHelperImpl(claimedRoles, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ClaimedRoles" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ClaimedRolesListType addNewClaimedRoles() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ClaimedRolesListType target = null;
            target = (org.etsi.uri.x01903.v13.ClaimedRolesListType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "ClaimedRoles" element
     */
    @Override
    public void unsetClaimedRoles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "CertifiedRoles" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertifiedRolesListType getCertifiedRoles() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertifiedRolesListType target = null;
            target = (org.etsi.uri.x01903.v13.CertifiedRolesListType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "CertifiedRoles" element
     */
    @Override
    public boolean isSetCertifiedRoles() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "CertifiedRoles" element
     */
    @Override
    public void setCertifiedRoles(org.etsi.uri.x01903.v13.CertifiedRolesListType certifiedRoles) {
        generatedSetterHelperImpl(certifiedRoles, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CertifiedRoles" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertifiedRolesListType addNewCertifiedRoles() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertifiedRolesListType target = null;
            target = (org.etsi.uri.x01903.v13.CertifiedRolesListType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "CertifiedRoles" element
     */
    @Override
    public void unsetCertifiedRoles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
