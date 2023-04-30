/*
 * XML Type:  SignatureMethodType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignatureMethodType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SignatureMethodType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class SignatureMethodTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignatureMethodType {
    private static final long serialVersionUID = 1L;

    public SignatureMethodTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "HMACOutputLength"),
        new QName("", "Algorithm"),
    };


    /**
     * Gets the "HMACOutputLength" element
     */
    @Override
    public java.math.BigInteger getHMACOutputLength() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "HMACOutputLength" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.HMACOutputLengthType xgetHMACOutputLength() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.HMACOutputLengthType target = null;
            target = (org.w3.x2000.x09.xmldsig.HMACOutputLengthType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * True if has "HMACOutputLength" element
     */
    @Override
    public boolean isSetHMACOutputLength() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "HMACOutputLength" element
     */
    @Override
    public void setHMACOutputLength(java.math.BigInteger hmacOutputLength) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setBigIntegerValue(hmacOutputLength);
        }
    }

    /**
     * Sets (as xml) the "HMACOutputLength" element
     */
    @Override
    public void xsetHMACOutputLength(org.w3.x2000.x09.xmldsig.HMACOutputLengthType hmacOutputLength) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.HMACOutputLengthType target = null;
            target = (org.w3.x2000.x09.xmldsig.HMACOutputLengthType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.w3.x2000.x09.xmldsig.HMACOutputLengthType)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(hmacOutputLength);
        }
    }

    /**
     * Unsets the "HMACOutputLength" element
     */
    @Override
    public void unsetHMACOutputLength() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "Algorithm" attribute
     */
    @Override
    public java.lang.String getAlgorithm() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Algorithm" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI xgetAlgorithm() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "Algorithm" attribute
     */
    @Override
    public void setAlgorithm(java.lang.String algorithm) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(algorithm);
        }
    }

    /**
     * Sets (as xml) the "Algorithm" attribute
     */
    @Override
    public void xsetAlgorithm(org.apache.xmlbeans.XmlAnyURI algorithm) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(algorithm);
        }
    }
}
