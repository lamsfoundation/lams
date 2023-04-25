/*
 * An XML document type.
 * Localname: Transform
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.TransformDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one Transform(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class TransformDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.TransformDocument {
    private static final long serialVersionUID = 1L;

    public TransformDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "Transform"),
    };


    /**
     * Gets the "Transform" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.TransformType getTransform() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.TransformType target = null;
            target = (org.w3.x2000.x09.xmldsig.TransformType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "Transform" element
     */
    @Override
    public void setTransform(org.w3.x2000.x09.xmldsig.TransformType transform) {
        generatedSetterHelperImpl(transform, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Transform" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.TransformType addNewTransform() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.TransformType target = null;
            target = (org.w3.x2000.x09.xmldsig.TransformType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
