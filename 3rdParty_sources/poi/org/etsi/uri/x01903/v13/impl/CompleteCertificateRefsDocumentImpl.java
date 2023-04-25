/*
 * An XML document type.
 * Localname: CompleteCertificateRefs
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CompleteCertificateRefsDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one CompleteCertificateRefs(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class CompleteCertificateRefsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CompleteCertificateRefsDocument {
    private static final long serialVersionUID = 1L;

    public CompleteCertificateRefsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CompleteCertificateRefs"),
    };


    /**
     * Gets the "CompleteCertificateRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteCertificateRefsType getCompleteCertificateRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteCertificateRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteCertificateRefsType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "CompleteCertificateRefs" element
     */
    @Override
    public void setCompleteCertificateRefs(org.etsi.uri.x01903.v13.CompleteCertificateRefsType completeCertificateRefs) {
        generatedSetterHelperImpl(completeCertificateRefs, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CompleteCertificateRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteCertificateRefsType addNewCompleteCertificateRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteCertificateRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteCertificateRefsType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
