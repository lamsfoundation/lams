/*
 * An XML document type.
 * Localname: KeyValue
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.KeyValueDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one KeyValue(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class KeyValueDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.KeyValueDocument {
    private static final long serialVersionUID = 1L;

    public KeyValueDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "KeyValue"),
    };


    /**
     * Gets the "KeyValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.KeyValueType getKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.KeyValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.KeyValueType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "KeyValue" element
     */
    @Override
    public void setKeyValue(org.w3.x2000.x09.xmldsig.KeyValueType keyValue) {
        generatedSetterHelperImpl(keyValue, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "KeyValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.KeyValueType addNewKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.KeyValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.KeyValueType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
