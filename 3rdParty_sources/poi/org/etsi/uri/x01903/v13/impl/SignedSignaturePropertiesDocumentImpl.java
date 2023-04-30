/*
 * An XML document type.
 * Localname: SignedSignatureProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignedSignaturePropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SignedSignatureProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class SignedSignaturePropertiesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SignedSignaturePropertiesDocument {
    private static final long serialVersionUID = 1L;

    public SignedSignaturePropertiesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignedSignatureProperties"),
    };


    /**
     * Gets the "SignedSignatureProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignedSignaturePropertiesType getSignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignedSignaturePropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.SignedSignaturePropertiesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignedSignatureProperties" element
     */
    @Override
    public void setSignedSignatureProperties(org.etsi.uri.x01903.v13.SignedSignaturePropertiesType signedSignatureProperties) {
        generatedSetterHelperImpl(signedSignatureProperties, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignedSignatureProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignedSignaturePropertiesType addNewSignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignedSignaturePropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.SignedSignaturePropertiesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
