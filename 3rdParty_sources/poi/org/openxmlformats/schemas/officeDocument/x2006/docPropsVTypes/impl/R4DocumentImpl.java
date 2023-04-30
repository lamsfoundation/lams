/*
 * An XML document type.
 * Localname: r4
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.R4Document
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one r4(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public class R4DocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.R4Document {
    private static final long serialVersionUID = 1L;

    public R4DocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "r4"),
    };


    /**
     * Gets the "r4" element
     */
    @Override
    public float getR4() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? 0.0f : target.getFloatValue();
        }
    }

    /**
     * Gets (as xml) the "r4" element
     */
    @Override
    public org.apache.xmlbeans.XmlFloat xgetR4() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * Sets the "r4" element
     */
    @Override
    public void setR4(float r4) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setFloatValue(r4);
        }
    }

    /**
     * Sets (as xml) the "r4" element
     */
    @Override
    public void xsetR4(org.apache.xmlbeans.XmlFloat r4) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlFloat)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(r4);
        }
    }
}
