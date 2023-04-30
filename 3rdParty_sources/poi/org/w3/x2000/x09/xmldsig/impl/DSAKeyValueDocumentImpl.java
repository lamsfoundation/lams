/*
 * An XML document type.
 * Localname: DSAKeyValue
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.DSAKeyValueDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one DSAKeyValue(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class DSAKeyValueDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.DSAKeyValueDocument {
    private static final long serialVersionUID = 1L;

    public DSAKeyValueDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "DSAKeyValue"),
    };


    /**
     * Gets the "DSAKeyValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.DSAKeyValueType getDSAKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DSAKeyValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.DSAKeyValueType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "DSAKeyValue" element
     */
    @Override
    public void setDSAKeyValue(org.w3.x2000.x09.xmldsig.DSAKeyValueType dsaKeyValue) {
        generatedSetterHelperImpl(dsaKeyValue, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "DSAKeyValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.DSAKeyValueType addNewDSAKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DSAKeyValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.DSAKeyValueType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
