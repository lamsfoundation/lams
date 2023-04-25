/*
 * XML Type:  CompleteRevocationRefsType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CompleteRevocationRefsType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CompleteRevocationRefsType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class CompleteRevocationRefsTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CompleteRevocationRefsType {
    private static final long serialVersionUID = 1L;

    public CompleteRevocationRefsTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CRLRefs"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "OCSPRefs"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "OtherRefs"),
        new QName("", "Id"),
    };


    /**
     * Gets the "CRLRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CRLRefsType getCRLRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CRLRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CRLRefsType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "CRLRefs" element
     */
    @Override
    public boolean isSetCRLRefs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "CRLRefs" element
     */
    @Override
    public void setCRLRefs(org.etsi.uri.x01903.v13.CRLRefsType crlRefs) {
        generatedSetterHelperImpl(crlRefs, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CRLRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CRLRefsType addNewCRLRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CRLRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CRLRefsType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "CRLRefs" element
     */
    @Override
    public void unsetCRLRefs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "OCSPRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OCSPRefsType getOCSPRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OCSPRefsType target = null;
            target = (org.etsi.uri.x01903.v13.OCSPRefsType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "OCSPRefs" element
     */
    @Override
    public boolean isSetOCSPRefs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "OCSPRefs" element
     */
    @Override
    public void setOCSPRefs(org.etsi.uri.x01903.v13.OCSPRefsType ocspRefs) {
        generatedSetterHelperImpl(ocspRefs, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "OCSPRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OCSPRefsType addNewOCSPRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OCSPRefsType target = null;
            target = (org.etsi.uri.x01903.v13.OCSPRefsType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "OCSPRefs" element
     */
    @Override
    public void unsetOCSPRefs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "OtherRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OtherCertStatusRefsType getOtherRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OtherCertStatusRefsType target = null;
            target = (org.etsi.uri.x01903.v13.OtherCertStatusRefsType)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "OtherRefs" element
     */
    @Override
    public boolean isSetOtherRefs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "OtherRefs" element
     */
    @Override
    public void setOtherRefs(org.etsi.uri.x01903.v13.OtherCertStatusRefsType otherRefs) {
        generatedSetterHelperImpl(otherRefs, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "OtherRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OtherCertStatusRefsType addNewOtherRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OtherCertStatusRefsType target = null;
            target = (org.etsi.uri.x01903.v13.OtherCertStatusRefsType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "OtherRefs" element
     */
    @Override
    public void unsetOtherRefs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[3]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[3]);
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
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}
