/*
 * An XML document type.
 * Localname: MgmtData
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.MgmtDataDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one MgmtData(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class MgmtDataDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.MgmtDataDocument {
    private static final long serialVersionUID = 1L;

    public MgmtDataDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "MgmtData"),
    };


    /**
     * Gets the "MgmtData" element
     */
    @Override
    public java.lang.String getMgmtData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "MgmtData" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetMgmtData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * Sets the "MgmtData" element
     */
    @Override
    public void setMgmtData(java.lang.String mgmtData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(mgmtData);
        }
    }

    /**
     * Sets (as xml) the "MgmtData" element
     */
    @Override
    public void xsetMgmtData(org.apache.xmlbeans.XmlString mgmtData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(mgmtData);
        }
    }
}
