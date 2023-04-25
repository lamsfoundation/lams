/*
 * An XML document type.
 * Localname: SignatureTime
 * Namespace: http://schemas.openxmlformats.org/package/2006/digital-signature
 * Java type: org.openxmlformats.schemas.xpackage.x2006.digitalSignature.SignatureTimeDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.digitalSignature.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SignatureTime(@http://schemas.openxmlformats.org/package/2006/digital-signature) element.
 *
 * This is a complex type.
 */
public class SignatureTimeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.xpackage.x2006.digitalSignature.SignatureTimeDocument {
    private static final long serialVersionUID = 1L;

    public SignatureTimeDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/package/2006/digital-signature", "SignatureTime"),
    };


    /**
     * Gets the "SignatureTime" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime getSignatureTime() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignatureTime" element
     */
    @Override
    public void setSignatureTime(org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime signatureTime) {
        generatedSetterHelperImpl(signatureTime, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignatureTime" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime addNewSignatureTime() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
