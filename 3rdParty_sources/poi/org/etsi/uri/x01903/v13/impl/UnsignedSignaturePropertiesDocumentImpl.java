/*
 * An XML document type.
 * Localname: UnsignedSignatureProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one UnsignedSignatureProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class UnsignedSignaturePropertiesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesDocument {
    private static final long serialVersionUID = 1L;

    public UnsignedSignaturePropertiesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "UnsignedSignatureProperties"),
    };


    /**
     * Gets the "UnsignedSignatureProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType getUnsignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "UnsignedSignatureProperties" element
     */
    @Override
    public void setUnsignedSignatureProperties(org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType unsignedSignatureProperties) {
        generatedSetterHelperImpl(unsignedSignatureProperties, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "UnsignedSignatureProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType addNewUnsignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
