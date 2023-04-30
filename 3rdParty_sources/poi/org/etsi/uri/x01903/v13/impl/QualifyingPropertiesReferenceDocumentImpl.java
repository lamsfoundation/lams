/*
 * An XML document type.
 * Localname: QualifyingPropertiesReference
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one QualifyingPropertiesReference(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class QualifyingPropertiesReferenceDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceDocument {
    private static final long serialVersionUID = 1L;

    public QualifyingPropertiesReferenceDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "QualifyingPropertiesReference"),
    };


    /**
     * Gets the "QualifyingPropertiesReference" element
     */
    @Override
    public org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceType getQualifyingPropertiesReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceType target = null;
            target = (org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "QualifyingPropertiesReference" element
     */
    @Override
    public void setQualifyingPropertiesReference(org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceType qualifyingPropertiesReference) {
        generatedSetterHelperImpl(qualifyingPropertiesReference, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "QualifyingPropertiesReference" element
     */
    @Override
    public org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceType addNewQualifyingPropertiesReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceType target = null;
            target = (org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
