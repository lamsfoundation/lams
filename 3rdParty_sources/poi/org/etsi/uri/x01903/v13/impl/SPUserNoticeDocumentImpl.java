/*
 * An XML document type.
 * Localname: SPUserNotice
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SPUserNoticeDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SPUserNotice(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class SPUserNoticeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SPUserNoticeDocument {
    private static final long serialVersionUID = 1L;

    public SPUserNoticeDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SPUserNotice"),
    };


    /**
     * Gets the "SPUserNotice" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SPUserNoticeType getSPUserNotice() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SPUserNoticeType target = null;
            target = (org.etsi.uri.x01903.v13.SPUserNoticeType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SPUserNotice" element
     */
    @Override
    public void setSPUserNotice(org.etsi.uri.x01903.v13.SPUserNoticeType spUserNotice) {
        generatedSetterHelperImpl(spUserNotice, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SPUserNotice" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SPUserNoticeType addNewSPUserNotice() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SPUserNoticeType target = null;
            target = (org.etsi.uri.x01903.v13.SPUserNoticeType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
