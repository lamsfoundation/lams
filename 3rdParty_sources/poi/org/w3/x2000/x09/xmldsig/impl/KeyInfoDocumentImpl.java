/*
 * An XML document type.
 * Localname: KeyInfo
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.KeyInfoDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one KeyInfo(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class KeyInfoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.KeyInfoDocument {
    private static final long serialVersionUID = 1L;

    public KeyInfoDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfo"),
    };


    /**
     * Gets the "KeyInfo" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.KeyInfoType getKeyInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.KeyInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.KeyInfoType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "KeyInfo" element
     */
    @Override
    public void setKeyInfo(org.w3.x2000.x09.xmldsig.KeyInfoType keyInfo) {
        generatedSetterHelperImpl(keyInfo, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "KeyInfo" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.KeyInfoType addNewKeyInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.KeyInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.KeyInfoType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
