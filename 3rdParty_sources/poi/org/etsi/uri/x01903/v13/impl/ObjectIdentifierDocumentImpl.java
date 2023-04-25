/*
 * An XML document type.
 * Localname: ObjectIdentifier
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.ObjectIdentifierDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one ObjectIdentifier(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class ObjectIdentifierDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.ObjectIdentifierDocument {
    private static final long serialVersionUID = 1L;

    public ObjectIdentifierDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "ObjectIdentifier"),
    };


    /**
     * Gets the "ObjectIdentifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ObjectIdentifierType getObjectIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ObjectIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.ObjectIdentifierType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "ObjectIdentifier" element
     */
    @Override
    public void setObjectIdentifier(org.etsi.uri.x01903.v13.ObjectIdentifierType objectIdentifier) {
        generatedSetterHelperImpl(objectIdentifier, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ObjectIdentifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ObjectIdentifierType addNewObjectIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ObjectIdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.ObjectIdentifierType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
