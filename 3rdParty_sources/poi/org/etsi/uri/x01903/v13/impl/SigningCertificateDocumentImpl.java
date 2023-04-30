/*
 * An XML document type.
 * Localname: SigningCertificate
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SigningCertificateDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SigningCertificate(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class SigningCertificateDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SigningCertificateDocument {
    private static final long serialVersionUID = 1L;

    public SigningCertificateDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SigningCertificate"),
    };


    /**
     * Gets the "SigningCertificate" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertIDListType getSigningCertificate() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertIDListType target = null;
            target = (org.etsi.uri.x01903.v13.CertIDListType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SigningCertificate" element
     */
    @Override
    public void setSigningCertificate(org.etsi.uri.x01903.v13.CertIDListType signingCertificate) {
        generatedSetterHelperImpl(signingCertificate, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SigningCertificate" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertIDListType addNewSigningCertificate() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertIDListType target = null;
            target = (org.etsi.uri.x01903.v13.CertIDListType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
