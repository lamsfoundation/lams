/*
 * An XML document type.
 * Localname: clsid
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.ClsidDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one clsid(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public class ClsidDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.ClsidDocument {
    private static final long serialVersionUID = 1L;

    public ClsidDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "clsid"),
    };


    /**
     * Gets the "clsid" element
     */
    @Override
    public java.lang.String getClsid() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "clsid" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetClsid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * Sets the "clsid" element
     */
    @Override
    public void setClsid(java.lang.String clsid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(clsid);
        }
    }

    /**
     * Sets (as xml) the "clsid" element
     */
    @Override
    public void xsetClsid(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid clsid) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(clsid);
        }
    }
}
