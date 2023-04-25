/*
 * An XML document type.
 * Localname: RetrievalMethod
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.RetrievalMethodDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one RetrievalMethod(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class RetrievalMethodDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.RetrievalMethodDocument {
    private static final long serialVersionUID = 1L;

    public RetrievalMethodDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethod"),
    };


    /**
     * Gets the "RetrievalMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.RetrievalMethodType getRetrievalMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.RetrievalMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.RetrievalMethodType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "RetrievalMethod" element
     */
    @Override
    public void setRetrievalMethod(org.w3.x2000.x09.xmldsig.RetrievalMethodType retrievalMethod) {
        generatedSetterHelperImpl(retrievalMethod, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "RetrievalMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.RetrievalMethodType addNewRetrievalMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.RetrievalMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.RetrievalMethodType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
