/*
 * An XML document type.
 * Localname: AttributeRevocationRefs
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.AttributeRevocationRefsDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one AttributeRevocationRefs(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class AttributeRevocationRefsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.AttributeRevocationRefsDocument {
    private static final long serialVersionUID = 1L;

    public AttributeRevocationRefsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "AttributeRevocationRefs"),
    };


    /**
     * Gets the "AttributeRevocationRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteRevocationRefsType getAttributeRevocationRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteRevocationRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteRevocationRefsType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "AttributeRevocationRefs" element
     */
    @Override
    public void setAttributeRevocationRefs(org.etsi.uri.x01903.v13.CompleteRevocationRefsType attributeRevocationRefs) {
        generatedSetterHelperImpl(attributeRevocationRefs, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "AttributeRevocationRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteRevocationRefsType addNewAttributeRevocationRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteRevocationRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteRevocationRefsType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
