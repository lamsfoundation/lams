/*
 * XML Type:  CT_SignatureTime
 * Namespace: http://schemas.openxmlformats.org/package/2006/digital-signature
 * Java type: org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.digitalSignature.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SignatureTime(@http://schemas.openxmlformats.org/package/2006/digital-signature).
 *
 * This is a complex type.
 */
public class CTSignatureTimeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime {
    private static final long serialVersionUID = 1L;

    public CTSignatureTimeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/package/2006/digital-signature", "Format"),
        new QName("http://schemas.openxmlformats.org/package/2006/digital-signature", "Value"),
    };


    /**
     * Gets the "Format" element
     */
    @Override
    public java.lang.String getFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Format" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STFormat xgetFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STFormat target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STFormat)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * Sets the "Format" element
     */
    @Override
    public void setFormat(java.lang.String format) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(format);
        }
    }

    /**
     * Sets (as xml) the "Format" element
     */
    @Override
    public void xsetFormat(org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STFormat format) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STFormat target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STFormat)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STFormat)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(format);
        }
    }

    /**
     * Gets the "Value" element
     */
    @Override
    public java.lang.String getValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Value" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STValue xgetValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STValue target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * Sets the "Value" element
     */
    @Override
    public void setValue(java.lang.String value) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(value);
        }
    }

    /**
     * Sets (as xml) the "Value" element
     */
    @Override
    public void xsetValue(org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STValue value) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STValue target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(value);
        }
    }
}
