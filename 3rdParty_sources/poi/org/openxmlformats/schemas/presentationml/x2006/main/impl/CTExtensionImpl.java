/*
 * XML Type:  CT_Extension
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTExtension
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Extension(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTExtensionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTExtension {
    private static final long serialVersionUID = 1L;

    public CTExtensionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "uri"),
    };


    /**
     * Gets the "uri" attribute
     */
    @Override
    public java.lang.String getUri() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "uri" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlToken xgetUri() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlToken target = null;
            target = (org.apache.xmlbeans.XmlToken)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "uri" attribute
     */
    @Override
    public void setUri(java.lang.String uri) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(uri);
        }
    }

    /**
     * Sets (as xml) the "uri" attribute
     */
    @Override
    public void xsetUri(org.apache.xmlbeans.XmlToken uri) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlToken target = null;
            target = (org.apache.xmlbeans.XmlToken)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlToken)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(uri);
        }
    }
}
