/*
 * An XML document type.
 * Localname: Transforms
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.TransformsDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one Transforms(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class TransformsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.TransformsDocument {
    private static final long serialVersionUID = 1L;

    public TransformsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "Transforms"),
    };


    /**
     * Gets the "Transforms" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.TransformsType getTransforms() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.TransformsType target = null;
            target = (org.w3.x2000.x09.xmldsig.TransformsType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "Transforms" element
     */
    @Override
    public void setTransforms(org.w3.x2000.x09.xmldsig.TransformsType transforms) {
        generatedSetterHelperImpl(transforms, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Transforms" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.TransformsType addNewTransforms() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.TransformsType target = null;
            target = (org.w3.x2000.x09.xmldsig.TransformsType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
