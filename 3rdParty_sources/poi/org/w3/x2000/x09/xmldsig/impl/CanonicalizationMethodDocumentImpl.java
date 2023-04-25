/*
 * An XML document type.
 * Localname: CanonicalizationMethod
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.CanonicalizationMethodDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one CanonicalizationMethod(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class CanonicalizationMethodDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.CanonicalizationMethodDocument {
    private static final long serialVersionUID = 1L;

    public CanonicalizationMethodDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethod"),
    };


    /**
     * Gets the "CanonicalizationMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.CanonicalizationMethodType getCanonicalizationMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CanonicalizationMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.CanonicalizationMethodType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "CanonicalizationMethod" element
     */
    @Override
    public void setCanonicalizationMethod(org.w3.x2000.x09.xmldsig.CanonicalizationMethodType canonicalizationMethod) {
        generatedSetterHelperImpl(canonicalizationMethod, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CanonicalizationMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.CanonicalizationMethodType addNewCanonicalizationMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CanonicalizationMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.CanonicalizationMethodType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
