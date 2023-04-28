/*
 * An XML document type.
 * Localname: SignatureProperty
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignaturePropertyDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SignatureProperty(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class SignaturePropertyDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignaturePropertyDocument {
    private static final long serialVersionUID = 1L;

    public SignaturePropertyDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureProperty"),
    };


    /**
     * Gets the "SignatureProperty" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignaturePropertyType getSignatureProperty() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignaturePropertyType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignaturePropertyType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignatureProperty" element
     */
    @Override
    public void setSignatureProperty(org.w3.x2000.x09.xmldsig.SignaturePropertyType signatureProperty) {
        generatedSetterHelperImpl(signatureProperty, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignatureProperty" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignaturePropertyType addNewSignatureProperty() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignaturePropertyType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignaturePropertyType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
