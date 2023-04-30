/*
 * An XML document type.
 * Localname: SignedProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignedPropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SignedProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class SignedPropertiesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SignedPropertiesDocument {
    private static final long serialVersionUID = 1L;

    public SignedPropertiesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignedProperties"),
    };


    /**
     * Gets the "SignedProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignedPropertiesType getSignedProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignedPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.SignedPropertiesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignedProperties" element
     */
    @Override
    public void setSignedProperties(org.etsi.uri.x01903.v13.SignedPropertiesType signedProperties) {
        generatedSetterHelperImpl(signedProperties, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignedProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignedPropertiesType addNewSignedProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignedPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.SignedPropertiesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
