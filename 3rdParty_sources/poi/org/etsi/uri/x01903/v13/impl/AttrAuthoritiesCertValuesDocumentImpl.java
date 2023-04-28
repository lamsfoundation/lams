/*
 * An XML document type.
 * Localname: AttrAuthoritiesCertValues
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.AttrAuthoritiesCertValuesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one AttrAuthoritiesCertValues(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class AttrAuthoritiesCertValuesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.AttrAuthoritiesCertValuesDocument {
    private static final long serialVersionUID = 1L;

    public AttrAuthoritiesCertValuesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "AttrAuthoritiesCertValues"),
    };


    /**
     * Gets the "AttrAuthoritiesCertValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertificateValuesType getAttrAuthoritiesCertValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertificateValuesType target = null;
            target = (org.etsi.uri.x01903.v13.CertificateValuesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "AttrAuthoritiesCertValues" element
     */
    @Override
    public void setAttrAuthoritiesCertValues(org.etsi.uri.x01903.v13.CertificateValuesType attrAuthoritiesCertValues) {
        generatedSetterHelperImpl(attrAuthoritiesCertValues, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "AttrAuthoritiesCertValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertificateValuesType addNewAttrAuthoritiesCertValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertificateValuesType target = null;
            target = (org.etsi.uri.x01903.v13.CertificateValuesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
