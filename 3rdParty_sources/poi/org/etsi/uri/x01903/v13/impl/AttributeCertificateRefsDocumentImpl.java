/*
 * An XML document type.
 * Localname: AttributeCertificateRefs
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.AttributeCertificateRefsDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one AttributeCertificateRefs(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class AttributeCertificateRefsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.AttributeCertificateRefsDocument {
    private static final long serialVersionUID = 1L;

    public AttributeCertificateRefsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "AttributeCertificateRefs"),
    };


    /**
     * Gets the "AttributeCertificateRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteCertificateRefsType getAttributeCertificateRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteCertificateRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteCertificateRefsType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "AttributeCertificateRefs" element
     */
    @Override
    public void setAttributeCertificateRefs(org.etsi.uri.x01903.v13.CompleteCertificateRefsType attributeCertificateRefs) {
        generatedSetterHelperImpl(attributeCertificateRefs, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "AttributeCertificateRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteCertificateRefsType addNewAttributeCertificateRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteCertificateRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteCertificateRefsType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
