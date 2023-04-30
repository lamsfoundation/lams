/*
 * An XML document type.
 * Localname: Signature
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignatureDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one Signature(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class SignatureDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignatureDocument {
    private static final long serialVersionUID = 1L;

    public SignatureDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
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
