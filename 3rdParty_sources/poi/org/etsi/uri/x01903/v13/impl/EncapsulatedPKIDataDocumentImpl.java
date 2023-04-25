/*
 * An XML document type.
 * Localname: EncapsulatedPKIData
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.EncapsulatedPKIDataDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one EncapsulatedPKIData(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class EncapsulatedPKIDataDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.EncapsulatedPKIDataDocument {
    private static final long serialVersionUID = 1L;

    public EncapsulatedPKIDataDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "EncapsulatedPKIData"),
    };


    /**
     * Gets the "EncapsulatedPKIData" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType getEncapsulatedPKIData() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "EncapsulatedPKIData" element
     */
    @Override
    public void setEncapsulatedPKIData(org.etsi.uri.x01903.v13.EncapsulatedPKIDataType encapsulatedPKIData) {
        generatedSetterHelperImpl(encapsulatedPKIData, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "EncapsulatedPKIData" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType addNewEncapsulatedPKIData() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
