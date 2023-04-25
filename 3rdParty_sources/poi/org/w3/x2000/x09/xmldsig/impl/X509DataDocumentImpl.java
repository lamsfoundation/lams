/*
 * An XML document type.
 * Localname: X509Data
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.X509DataDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one X509Data(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class X509DataDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.X509DataDocument {
    private static final long serialVersionUID = 1L;

    public X509DataDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "X509Data"),
    };


    /**
     * Gets the "X509Data" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.X509DataType getX509Data() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509DataType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509DataType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "X509Data" element
     */
    @Override
    public void setX509Data(org.w3.x2000.x09.xmldsig.X509DataType x509Data) {
        generatedSetterHelperImpl(x509Data, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "X509Data" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.X509DataType addNewX509Data() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509DataType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509DataType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
