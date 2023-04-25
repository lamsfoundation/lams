/*
 * An XML document type.
 * Localname: DataObjectFormat
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.DataObjectFormatDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one DataObjectFormat(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class DataObjectFormatDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.DataObjectFormatDocument {
    private static final long serialVersionUID = 1L;

    public DataObjectFormatDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "DataObjectFormat"),
    };


    /**
     * Gets the "DataObjectFormat" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DataObjectFormatType getDataObjectFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DataObjectFormatType target = null;
            target = (org.etsi.uri.x01903.v13.DataObjectFormatType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "DataObjectFormat" element
     */
    @Override
    public void setDataObjectFormat(org.etsi.uri.x01903.v13.DataObjectFormatType dataObjectFormat) {
        generatedSetterHelperImpl(dataObjectFormat, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "DataObjectFormat" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DataObjectFormatType addNewDataObjectFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DataObjectFormatType target = null;
            target = (org.etsi.uri.x01903.v13.DataObjectFormatType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
