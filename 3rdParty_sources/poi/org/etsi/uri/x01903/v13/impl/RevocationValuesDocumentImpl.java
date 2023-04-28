/*
 * An XML document type.
 * Localname: RevocationValues
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.RevocationValuesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one RevocationValues(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class RevocationValuesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.RevocationValuesDocument {
    private static final long serialVersionUID = 1L;

    public RevocationValuesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "RevocationValues"),
    };


    /**
     * Gets the "RevocationValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.RevocationValuesType getRevocationValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.RevocationValuesType target = null;
            target = (org.etsi.uri.x01903.v13.RevocationValuesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "RevocationValues" element
     */
    @Override
    public void setRevocationValues(org.etsi.uri.x01903.v13.RevocationValuesType revocationValues) {
        generatedSetterHelperImpl(revocationValues, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "RevocationValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.RevocationValuesType addNewRevocationValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.RevocationValuesType target = null;
            target = (org.etsi.uri.x01903.v13.RevocationValuesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
