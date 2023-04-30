/*
 * An XML document type.
 * Localname: storage
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.StorageDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one storage(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public class StorageDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.StorageDocument {
    private static final long serialVersionUID = 1L;

    public StorageDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "storage"),
    };


    /**
     * Gets the "storage" element
     */
    @Override
    public byte[] getStorage() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "storage" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetStorage() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * Sets the "storage" element
     */
    @Override
    public void setStorage(byte[] storage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setByteArrayValue(storage);
        }
    }

    /**
     * Sets (as xml) the "storage" element
     */
    @Override
    public void xsetStorage(org.apache.xmlbeans.XmlBase64Binary storage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(storage);
        }
    }
}
