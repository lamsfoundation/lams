/*
 * An XML document type.
 * Localname: headers
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.HeadersDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one headers(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class HeadersDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.HeadersDocument {
    private static final long serialVersionUID = 1L;

    public HeadersDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "headers"),
    };


    /**
     * Gets the "headers" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionHeaders getHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionHeaders target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionHeaders)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "headers" element
     */
    @Override
    public void setHeaders(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionHeaders headers) {
        generatedSetterHelperImpl(headers, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "headers" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionHeaders addNewHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionHeaders target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionHeaders)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
