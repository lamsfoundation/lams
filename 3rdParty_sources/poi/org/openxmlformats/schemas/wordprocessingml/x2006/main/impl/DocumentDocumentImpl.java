/*
 * An XML document type.
 * Localname: document
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.DocumentDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one document(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public class DocumentDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.DocumentDocument {
    private static final long serialVersionUID = 1L;

    public DocumentDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "document"),
    };


    /**
     * Gets the "document" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1 getDocument() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1 target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "document" element
     */
    @Override
    public void setDocument(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1 document) {
        generatedSetterHelperImpl(document, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "document" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1 addNewDocument() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1 target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
