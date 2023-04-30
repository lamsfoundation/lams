/*
 * An XML document type.
 * Localname: SignedInfo
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignedInfoDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SignedInfo(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class SignedInfoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignedInfoDocument {
    private static final long serialVersionUID = 1L;

    public SignedInfoDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "SignedInfo"),
    };


    /**
     * Gets the "SignedInfo" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignedInfoType getSignedInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignedInfo" element
     */
    @Override
    public void setSignedInfo(org.w3.x2000.x09.xmldsig.SignedInfoType signedInfo) {
        generatedSetterHelperImpl(signedInfo, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignedInfo" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignedInfoType addNewSignedInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
