/*
 * XML Type:  X509IssuerSerialType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.X509IssuerSerialType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML X509IssuerSerialType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class X509IssuerSerialTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.X509IssuerSerialType {
    private static final long serialVersionUID = 1L;

    public X509IssuerSerialTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "X509IssuerName"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "X509SerialNumber"),
    };


    /**
     * Gets the "X509IssuerName" element
     */
    @Override
    public java.lang.String getX509IssuerName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "X509IssuerName" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetX509IssuerName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * Sets the "X509IssuerName" element
     */
    @Override
    public void setX509IssuerName(java.lang.String x509IssuerName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(x509IssuerName);
        }
    }

    /**
     * Sets (as xml) the "X509IssuerName" element
     */
    @Override
    public void xsetX509IssuerName(org.apache.xmlbeans.XmlString x509IssuerName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(x509IssuerName);
        }
    }

    /**
     * Gets the "X509SerialNumber" element
     */
    @Override
    public java.math.BigInteger getX509SerialNumber() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "X509SerialNumber" element
     */
    @Override
    public org.apache.xmlbeans.XmlInteger xgetX509SerialNumber() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInteger target = null;
            target = (org.apache.xmlbeans.XmlInteger)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * Sets the "X509SerialNumber" element
     */
    @Override
    public void setX509SerialNumber(java.math.BigInteger x509SerialNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setBigIntegerValue(x509SerialNumber);
        }
    }

    /**
     * Sets (as xml) the "X509SerialNumber" element
     */
    @Override
    public void xsetX509SerialNumber(org.apache.xmlbeans.XmlInteger x509SerialNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInteger target = null;
            target = (org.apache.xmlbeans.XmlInteger)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInteger)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(x509SerialNumber);
        }
    }
}
