/*
 * An XML document type.
 * Localname: Object
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.ObjectDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one Object(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class ObjectDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.ObjectDocument {
    private static final long serialVersionUID = 1L;

    public ObjectDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "Object"),
    };


    /**
     * Gets the "Object" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ObjectType getObject() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ObjectType target = null;
            target = (org.w3.x2000.x09.xmldsig.ObjectType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "Object" element
     */
    @Override
    public void setObject(org.w3.x2000.x09.xmldsig.ObjectType object) {
        generatedSetterHelperImpl(object, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Object" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ObjectType addNewObject() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ObjectType target = null;
            target = (org.w3.x2000.x09.xmldsig.ObjectType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
