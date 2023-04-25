/*
 * XML Type:  RevocationValuesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.RevocationValuesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML RevocationValuesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class RevocationValuesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.RevocationValuesType {
    private static final long serialVersionUID = 1L;

    public RevocationValuesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CRLValues"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "OCSPValues"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "OtherValues"),
        new QName("", "Id"),
    };


    /**
     * Gets the "CRLValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CRLValuesType getCRLValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CRLValuesType target = null;
            target = (org.etsi.uri.x01903.v13.CRLValuesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "CRLValues" element
     */
    @Override
    public boolean isSetCRLValues() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "CRLValues" element
     */
    @Override
    public void setCRLValues(org.etsi.uri.x01903.v13.CRLValuesType crlValues) {
        generatedSetterHelperImpl(crlValues, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CRLValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CRLValuesType addNewCRLValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CRLValuesType target = null;
            target = (org.etsi.uri.x01903.v13.CRLValuesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "CRLValues" element
     */
    @Override
    public void unsetCRLValues() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "OCSPValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OCSPValuesType getOCSPValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OCSPValuesType target = null;
            target = (org.etsi.uri.x01903.v13.OCSPValuesType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "OCSPValues" element
     */
    @Override
    public boolean isSetOCSPValues() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "OCSPValues" element
     */
    @Override
    public void setOCSPValues(org.etsi.uri.x01903.v13.OCSPValuesType ocspValues) {
        generatedSetterHelperImpl(ocspValues, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "OCSPValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OCSPValuesType addNewOCSPValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OCSPValuesType target = null;
            target = (org.etsi.uri.x01903.v13.OCSPValuesType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "OCSPValues" element
     */
    @Override
    public void unsetOCSPValues() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "OtherValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OtherCertStatusValuesType getOtherValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OtherCertStatusValuesType target = null;
            target = (org.etsi.uri.x01903.v13.OtherCertStatusValuesType)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "OtherValues" element
     */
    @Override
    public boolean isSetOtherValues() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "OtherValues" element
     */
    @Override
    public void setOtherValues(org.etsi.uri.x01903.v13.OtherCertStatusValuesType otherValues) {
        generatedSetterHelperImpl(otherValues, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "OtherValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OtherCertStatusValuesType addNewOtherValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OtherCertStatusValuesType target = null;
            target = (org.etsi.uri.x01903.v13.OtherCertStatusValuesType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "OtherValues" element
     */
    @Override
    public void unsetOtherValues() {
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
