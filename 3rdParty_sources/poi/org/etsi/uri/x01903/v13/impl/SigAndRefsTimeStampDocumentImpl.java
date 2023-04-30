/*
 * An XML document type.
 * Localname: SigAndRefsTimeStamp
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SigAndRefsTimeStampDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SigAndRefsTimeStamp(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class SigAndRefsTimeStampDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SigAndRefsTimeStampDocument {
    private static final long serialVersionUID = 1L;

    public SigAndRefsTimeStampDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SigAndRefsTimeStamp"),
    };


    /**
     * Gets the "SigAndRefsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType getSigAndRefsTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SigAndRefsTimeStamp" element
     */
    @Override
    public void setSigAndRefsTimeStamp(org.etsi.uri.x01903.v13.XAdESTimeStampType sigAndRefsTimeStamp) {
        generatedSetterHelperImpl(sigAndRefsTimeStamp, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SigAndRefsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType addNewSigAndRefsTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
