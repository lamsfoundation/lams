/*
 * An XML document type.
 * Localname: SignedDataObjectProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignedDataObjectPropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SignedDataObjectProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class SignedDataObjectPropertiesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SignedDataObjectPropertiesDocument {
    private static final long serialVersionUID = 1L;

    public SignedDataObjectPropertiesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignedDataObjectProperties"),
    };


    /**
     * Gets the "SignedDataObjectProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType getSignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignedDataObjectProperties" element
     */
    @Override
    public void setSignedDataObjectProperties(org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType signedDataObjectProperties) {
        generatedSetterHelperImpl(signedDataObjectProperties, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignedDataObjectProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType addNewSignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
