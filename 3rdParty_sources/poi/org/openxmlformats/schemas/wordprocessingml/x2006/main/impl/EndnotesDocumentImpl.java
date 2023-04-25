/*
 * An XML document type.
 * Localname: endnotes
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.EndnotesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one endnotes(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public class EndnotesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.EndnotesDocument {
    private static final long serialVersionUID = 1L;

    public EndnotesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "endnotes"),
    };


    /**
     * Gets the "endnotes" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes getEndnotes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "endnotes" element
     */
    @Override
    public void setEndnotes(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes endnotes) {
        generatedSetterHelperImpl(endnotes, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "endnotes" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes addNewEndnotes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
