/*
 * An XML document type.
 * Localname: SignatureMethod
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignatureMethodDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SignatureMethod(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class SignatureMethodDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignatureMethodDocument {
    private static final long serialVersionUID = 1L;

    public SignatureMethodDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureMethod"),
    };


    /**
     * Gets the "SignatureMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignatureMethodType getSignatureMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureMethodType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignatureMethod" element
     */
    @Override
    public void setSignatureMethod(org.w3.x2000.x09.xmldsig.SignatureMethodType signatureMethod) {
        generatedSetterHelperImpl(signatureMethod, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignatureMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignatureMethodType addNewSignatureMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureMethodType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
