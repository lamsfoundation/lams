/*
 * An XML document type.
 * Localname: DigestValue
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.DigestValueDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one DigestValue(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class DigestValueDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.DigestValueDocument {
    private static final long serialVersionUID = 1L;

    public DigestValueDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "DigestValue"),
    };


    /**
     * Gets the "DigestValue" element
     */
    @Override
    public byte[] getDigestValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            target = (org.w3.x2000.x09.xmldsig.DigestValueType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            target = (org.w3.x2000.x09.xmldsig.DigestValueType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.w3.x2000.x09.xmldsig.DigestValueType)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(digestValue);
        }
    }
}
