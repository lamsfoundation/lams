/*
 * An XML document type.
 * Localname: OtherTimeStamp
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OtherTimeStampDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one OtherTimeStamp(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class OtherTimeStampDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.OtherTimeStampDocument {
    private static final long serialVersionUID = 1L;

    public OtherTimeStampDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "OtherTimeStamp"),
    };


    /**
     * Gets the "OtherTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OtherTimeStampType getOtherTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OtherTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.OtherTimeStampType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "OtherTimeStamp" element
     */
    @Override
    public void setOtherTimeStamp(org.etsi.uri.x01903.v13.OtherTimeStampType otherTimeStamp) {
        generatedSetterHelperImpl(otherTimeStamp, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "OtherTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.OtherTimeStampType addNewOtherTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.OtherTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.OtherTimeStampType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
