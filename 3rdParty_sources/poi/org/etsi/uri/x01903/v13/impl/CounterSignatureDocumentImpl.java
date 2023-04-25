/*
 * An XML document type.
 * Localname: CounterSignature
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CounterSignatureDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one CounterSignature(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class CounterSignatureDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CounterSignatureDocument {
    private static final long serialVersionUID = 1L;

    public CounterSignatureDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CounterSignature"),
    };


    /**
     * Gets the "CounterSignature" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CounterSignatureType getCounterSignature() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CounterSignatureType target = null;
            target = (org.etsi.uri.x01903.v13.CounterSignatureType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "CounterSignature" element
     */
    @Override
    public void setCounterSignature(org.etsi.uri.x01903.v13.CounterSignatureType counterSignature) {
        generatedSetterHelperImpl(counterSignature, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CounterSignature" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CounterSignatureType addNewCounterSignature() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CounterSignatureType target = null;
            target = (org.etsi.uri.x01903.v13.CounterSignatureType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
