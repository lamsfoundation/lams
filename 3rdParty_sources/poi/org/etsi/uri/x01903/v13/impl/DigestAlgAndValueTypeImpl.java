/*
 * XML Type:  DigestAlgAndValueType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.DigestAlgAndValueType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML DigestAlgAndValueType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class DigestAlgAndValueTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.DigestAlgAndValueType {
    private static final long serialVersionUID = 1L;

    public DigestAlgAndValueTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "DigestMethod"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "DigestValue"),
    };


    /**
     * Gets the "DigestMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.DigestMethodType getDigestMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DigestMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.DigestMethodType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "DigestMethod" element
     */
    @Override
    public void setDigestMethod(org.w3.x2000.x09.xmldsig.DigestMethodType digestMethod) {
        generatedSetterHelperImpl(digestMethod, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "DigestMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.DigestMethodType addNewDigestMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DigestMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.DigestMethodType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "DigestValue" element
     */
    @Override
    public byte[] getDigestValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "DigestValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.DigestValueType xgetDigestValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DigestValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.DigestValueType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * Sets the "DigestValue" element
     */
    @Override
    public void setDigestValue(byte[] digestValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setByteArrayValue(digestValue);
        }
    }

    /**
     * Sets (as xml) the "DigestValue" element
     */
    @Override
    public void xsetDigestValue(org.w3.x2000.x09.xmldsig.DigestValueType digestValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DigestValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.DigestValueType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.w3.x2000.x09.xmldsig.DigestValueType)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(digestValue);
        }
    }
}
