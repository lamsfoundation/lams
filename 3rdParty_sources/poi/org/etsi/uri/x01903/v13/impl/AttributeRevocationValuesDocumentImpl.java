/*
 * An XML document type.
 * Localname: AttributeRevocationValues
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.AttributeRevocationValuesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one AttributeRevocationValues(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class AttributeRevocationValuesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.AttributeRevocationValuesDocument {
    private static final long serialVersionUID = 1L;

    public AttributeRevocationValuesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "AttributeRevocationValues"),
    };


    /**
     * Gets the "AttributeRevocationValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.RevocationValuesType getAttributeRevocationValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.RevocationValuesType target = null;
            target = (org.etsi.uri.x01903.v13.RevocationValuesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "AttributeRevocationValues" element
     */
    @Override
    public void setAttributeRevocationValues(org.etsi.uri.x01903.v13.RevocationValuesType attributeRevocationValues) {
        generatedSetterHelperImpl(attributeRevocationValues, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "AttributeRevocationValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.RevocationValuesType addNewAttributeRevocationValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.RevocationValuesType target = null;
            target = (org.etsi.uri.x01903.v13.RevocationValuesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
