/*
 * An XML document type.
 * Localname: UnsignedProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.UnsignedPropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one UnsignedProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class UnsignedPropertiesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.UnsignedPropertiesDocument {
    private static final long serialVersionUID = 1L;

    public UnsignedPropertiesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "UnsignedProperties"),
    };


    /**
     * Gets the "UnsignedProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.UnsignedPropertiesType getUnsignedProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.UnsignedPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.UnsignedPropertiesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "UnsignedProperties" element
     */
    @Override
    public void setUnsignedProperties(org.etsi.uri.x01903.v13.UnsignedPropertiesType unsignedProperties) {
        generatedSetterHelperImpl(unsignedProperties, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "UnsignedProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.UnsignedPropertiesType addNewUnsignedProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.UnsignedPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.UnsignedPropertiesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
