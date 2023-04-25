/*
 * An XML document type.
 * Localname: RefsOnlyTimeStamp
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.RefsOnlyTimeStampDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one RefsOnlyTimeStamp(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class RefsOnlyTimeStampDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.RefsOnlyTimeStampDocument {
    private static final long serialVersionUID = 1L;

    public RefsOnlyTimeStampDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "RefsOnlyTimeStamp"),
    };


    /**
     * Gets the "RefsOnlyTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType getRefsOnlyTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "RefsOnlyTimeStamp" element
     */
    @Override
    public void setRefsOnlyTimeStamp(org.etsi.uri.x01903.v13.XAdESTimeStampType refsOnlyTimeStamp) {
        generatedSetterHelperImpl(refsOnlyTimeStamp, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "RefsOnlyTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType addNewRefsOnlyTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
