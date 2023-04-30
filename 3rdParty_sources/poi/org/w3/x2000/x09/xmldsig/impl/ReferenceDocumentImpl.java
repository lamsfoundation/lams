/*
 * An XML document type.
 * Localname: Reference
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.ReferenceDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one Reference(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class ReferenceDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.ReferenceDocument {
    private static final long serialVersionUID = 1L;

    public ReferenceDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "Reference"),
    };


    /**
     * Gets the "Reference" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ReferenceType getReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ReferenceType target = null;
            target = (org.w3.x2000.x09.xmldsig.ReferenceType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "Reference" element
     */
    @Override
    public void setReference(org.w3.x2000.x09.xmldsig.ReferenceType reference) {
        generatedSetterHelperImpl(reference, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Reference" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ReferenceType addNewReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ReferenceType target = null;
            target = (org.w3.x2000.x09.xmldsig.ReferenceType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
