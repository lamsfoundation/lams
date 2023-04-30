/*
 * An XML document type.
 * Localname: PGPData
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.PGPDataDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one PGPData(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class PGPDataDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.PGPDataDocument {
    private static final long serialVersionUID = 1L;

    public PGPDataDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "PGPData"),
    };


    /**
     * Gets the "PGPData" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.PGPDataType getPGPData() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.PGPDataType target = null;
            target = (org.w3.x2000.x09.xmldsig.PGPDataType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "PGPData" element
     */
    @Override
    public void setPGPData(org.w3.x2000.x09.xmldsig.PGPDataType pgpData) {
        generatedSetterHelperImpl(pgpData, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "PGPData" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.PGPDataType addNewPGPData() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.PGPDataType target = null;
            target = (org.w3.x2000.x09.xmldsig.PGPDataType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
