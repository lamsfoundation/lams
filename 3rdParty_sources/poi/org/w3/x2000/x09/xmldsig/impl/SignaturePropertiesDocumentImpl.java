/*
 * An XML document type.
 * Localname: SignatureProperties
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignaturePropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SignatureProperties(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class SignaturePropertiesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignaturePropertiesDocument {
    private static final long serialVersionUID = 1L;

    public SignaturePropertiesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureProperties"),
    };


    /**
     * Gets the "SignatureProperties" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignaturePropertiesType getSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignaturePropertiesType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignaturePropertiesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignatureProperties" element
     */
    @Override
    public void setSignatureProperties(org.w3.x2000.x09.xmldsig.SignaturePropertiesType signatureProperties) {
        generatedSetterHelperImpl(signatureProperties, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignatureProperties" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignaturePropertiesType addNewSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignaturePropertiesType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignaturePropertiesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
