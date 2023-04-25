/*
 * XML Type:  SignedSignaturePropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignedSignaturePropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SignedSignaturePropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class SignedSignaturePropertiesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SignedSignaturePropertiesType {
    private static final long serialVersionUID = 1L;

    public SignedSignaturePropertiesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SigningTime"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SigningCertificate"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignaturePolicyIdentifier"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignatureProductionPlace"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignerRole"),
        new QName("", "Id"),
    };


    /**
     * Gets the "SigningTime" element
     */
    @Override
    public java.util.Calendar getSigningTime() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getCalendarValue();
        }
    }

    /**
     * Gets (as xml) the "SigningTime" element
     */
    @Override
    public org.apache.xmlbeans.XmlDateTime xgetSigningTime() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * True if has "SigningTime" element
     */
    @Override
    public boolean isSetSigningTime() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "SigningTime" element
     */
    @Override
    public void setSigningTime(java.util.Calendar signingTime) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setCalendarValue(signingTime);
        }
    }

    /**
     * Sets (as xml) the "SigningTime" element
     */
    @Override
    public void xsetSigningTime(org.apache.xmlbeans.XmlDateTime signingTime) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDateTime)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(signingTime);
        }
    }

    /**
     * Unsets the "SigningTime" element
     */
    @Override
    public void unsetSigningTime() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "SigningCertificate" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertIDListType getSigningCertificate() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertIDListType target = null;
            target = (org.etsi.uri.x01903.v13.CertIDListType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "SigningCertificate" element
     */
    @Override
    public boolean isSetSigningCertificate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "SigningCertificate" element
     */
    @Override
    public void setSigningCertificate(org.etsi.uri.x01903.v13.CertIDListType signingCertificate) {
        generatedSetterHelperImpl(signingCertificate, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SigningCertificate" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertIDListType addNewSigningCertificate() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertIDListType target = null;
            target = (org.etsi.uri.x01903.v13.CertIDListType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "SigningCertificate" element
     */
    @Override
    public void unsetSigningCertificate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "SignaturePolicyIdentifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType getSignaturePolicyIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "SignaturePolicyIdentifier" element
     */
    @Override
    public boolean isSetSignaturePolicyIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "SignaturePolicyIdentifier" element
     */
    @Override
    public void setSignaturePolicyIdentifier(org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType signaturePolicyIdentifier) {
        generatedSetterHelperImpl(signaturePolicyIdentifier, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignaturePolicyIdentifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType addNewSignaturePolicyIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "SignaturePolicyIdentifier" element
     */
    @Override
    public void unsetSignaturePolicyIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "SignatureProductionPlace" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignatureProductionPlaceType getSignatureProductionPlace() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignatureProductionPlaceType target = null;
            target = (org.etsi.uri.x01903.v13.SignatureProductionPlaceType)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "SignatureProductionPlace" element
     */
    @Override
    public boolean isSetSignatureProductionPlace() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "SignatureProductionPlace" element
     */
    @Override
    public void setSignatureProductionPlace(org.etsi.uri.x01903.v13.SignatureProductionPlaceType signatureProductionPlace) {
        generatedSetterHelperImpl(signatureProductionPlace, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignatureProductionPlace" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignatureProductionPlaceType addNewSignatureProductionPlace() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignatureProductionPlaceType target = null;
            target = (org.etsi.uri.x01903.v13.SignatureProductionPlaceType)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "SignatureProductionPlace" element
     */
    @Override
    public void unsetSignatureProductionPlace() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "SignerRole" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignerRoleType getSignerRole() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignerRoleType target = null;
            target = (org.etsi.uri.x01903.v13.SignerRoleType)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "SignerRole" element
     */
    @Override
    public boolean isSetSignerRole() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "SignerRole" element
     */
    @Override
    public void setSignerRole(org.etsi.uri.x01903.v13.SignerRoleType signerRole) {
        generatedSetterHelperImpl(signerRole, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignerRole" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignerRoleType addNewSignerRole() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignerRoleType target = null;
            target = (org.etsi.uri.x01903.v13.SignerRoleType)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "SignerRole" element
     */
    @Override
    public void unsetSignerRole() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[5]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[5]);
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
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }
}
