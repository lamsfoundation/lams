/*
 * An XML document type.
 * Localname: glossaryDocument
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.GlossaryDocumentDocument1
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one glossaryDocument(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public class GlossaryDocumentDocument1Impl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.GlossaryDocumentDocument1 {
    private static final long serialVersionUID = 1L;

    public GlossaryDocumentDocument1Impl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "glossaryDocument"),
    };


    /**
     * Gets the "glossaryDocument" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1 getGlossaryDocument() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1 target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "glossaryDocument" element
     */
    @Override
    public void setGlossaryDocument(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1 glossaryDocument) {
        generatedSetterHelperImpl(glossaryDocument, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "glossaryDocument" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1 addNewGlossaryDocument() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1 target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
