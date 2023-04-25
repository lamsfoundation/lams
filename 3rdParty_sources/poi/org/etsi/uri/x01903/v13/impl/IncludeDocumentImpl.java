/*
 * An XML document type.
 * Localname: Include
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.IncludeDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one Include(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class IncludeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.IncludeDocument {
    private static final long serialVersionUID = 1L;

    public IncludeDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "Include"),
    };


    /**
     * Gets the "Include" element
     */
    @Override
    public org.etsi.uri.x01903.v13.IncludeType getInclude() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.IncludeType target = null;
            target = (org.etsi.uri.x01903.v13.IncludeType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "Include" element
     */
    @Override
    public void setInclude(org.etsi.uri.x01903.v13.IncludeType include) {
        generatedSetterHelperImpl(include, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Include" element
     */
    @Override
    public org.etsi.uri.x01903.v13.IncludeType addNewInclude() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.IncludeType target = null;
            target = (org.etsi.uri.x01903.v13.IncludeType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
