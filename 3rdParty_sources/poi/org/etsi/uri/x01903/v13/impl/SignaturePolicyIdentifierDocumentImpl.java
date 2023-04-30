/*
 * An XML document type.
 * Localname: SignaturePolicyIdentifier
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignaturePolicyIdentifierDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SignaturePolicyIdentifier(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class SignaturePolicyIdentifierDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SignaturePolicyIdentifierDocument {
    private static final long serialVersionUID = 1L;

    public SignaturePolicyIdentifierDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignaturePolicyIdentifier"),
    };


    /**
     * Gets the "SignaturePolicyIdentifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType getSignaturePolicyIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignaturePolicyIdentifier" element
     */
    @Override
    public void setSignaturePolicyIdentifier(org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType signaturePolicyIdentifier) {
        generatedSetterHelperImpl(signaturePolicyIdentifier, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignaturePolicyIdentifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType addNewSignaturePolicyIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
