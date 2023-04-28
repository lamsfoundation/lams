/*
 * An XML document type.
 * Localname: SPKIData
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SPKIDataDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SPKIData(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class SPKIDataDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SPKIDataDocument {
    private static final long serialVersionUID = 1L;

    public SPKIDataDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "SPKIData"),
    };


    /**
     * Gets the "SPKIData" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SPKIDataType getSPKIData() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SPKIDataType target = null;
            target = (org.w3.x2000.x09.xmldsig.SPKIDataType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SPKIData" element
     */
    @Override
    public void setSPKIData(org.w3.x2000.x09.xmldsig.SPKIDataType spkiData) {
        generatedSetterHelperImpl(spkiData, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SPKIData" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SPKIDataType addNewSPKIData() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SPKIDataType target = null;
            target = (org.w3.x2000.x09.xmldsig.SPKIDataType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
