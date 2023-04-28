/*
 * An XML document type.
 * Localname: CertificateValues
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CertificateValuesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one CertificateValues(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class CertificateValuesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CertificateValuesDocument {
    private static final long serialVersionUID = 1L;

    public CertificateValuesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CertificateValues"),
    };


    /**
     * Gets the "CertificateValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertificateValuesType getCertificateValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertificateValuesType target = null;
            target = (org.etsi.uri.x01903.v13.CertificateValuesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "CertificateValues" element
     */
    @Override
    public void setCertificateValues(org.etsi.uri.x01903.v13.CertificateValuesType certificateValues) {
        generatedSetterHelperImpl(certificateValues, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CertificateValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertificateValuesType addNewCertificateValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertificateValuesType target = null;
            target = (org.etsi.uri.x01903.v13.CertificateValuesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
