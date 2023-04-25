/*
 * An XML document type.
 * Localname: UnsignedDataObjectProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one UnsignedDataObjectProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class UnsignedDataObjectPropertiesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesDocument {
    private static final long serialVersionUID = 1L;

    public UnsignedDataObjectPropertiesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "UnsignedDataObjectProperties"),
    };


    /**
     * Gets the "UnsignedDataObjectProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType getUnsignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "UnsignedDataObjectProperties" element
     */
    @Override
    public void setUnsignedDataObjectProperties(org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType unsignedDataObjectProperties) {
        generatedSetterHelperImpl(unsignedDataObjectProperties, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "UnsignedDataObjectProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType addNewUnsignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
