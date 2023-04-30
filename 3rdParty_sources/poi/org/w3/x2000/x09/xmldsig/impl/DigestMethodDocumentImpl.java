/*
 * An XML document type.
 * Localname: DigestMethod
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.DigestMethodDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one DigestMethod(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class DigestMethodDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.DigestMethodDocument {
    private static final long serialVersionUID = 1L;

    public DigestMethodDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "DigestMethod"),
    };


    /**
     * Gets the "DigestMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.DigestMethodType getDigestMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DigestMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.DigestMethodType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "DigestMethod" element
     */
    @Override
    public void setDigestMethod(org.w3.x2000.x09.xmldsig.DigestMethodType digestMethod) {
        generatedSetterHelperImpl(digestMethod, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "DigestMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.DigestMethodType addNewDigestMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DigestMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.DigestMethodType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
