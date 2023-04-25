/*
 * XML Type:  CounterSignatureType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CounterSignatureType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CounterSignatureType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class CounterSignatureTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CounterSignatureType {
    private static final long serialVersionUID = 1L;

    public CounterSignatureTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "Signature"),
    };


    /**
     * Gets the "Signature" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignatureType getSignature() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "Signature" element
     */
    @Override
    public void setSignature(org.w3.x2000.x09.xmldsig.SignatureType signature) {
        generatedSetterHelperImpl(signature, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Signature" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignatureType addNewSignature() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
