/*
 * An XML document type.
 * Localname: SignatureValue
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignatureValueDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SignatureValue(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class SignatureValueDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignatureValueDocument {
    private static final long serialVersionUID = 1L;

    public SignatureValueDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureValue"),
    };


    /**
     * Gets the "SignatureValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignatureValueType getSignatureValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureValueType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignatureValue" element
     */
    @Override
    public void setSignatureValue(org.w3.x2000.x09.xmldsig.SignatureValueType signatureValue) {
        generatedSetterHelperImpl(signatureValue, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignatureValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignatureValueType addNewSignatureValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureValueType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
