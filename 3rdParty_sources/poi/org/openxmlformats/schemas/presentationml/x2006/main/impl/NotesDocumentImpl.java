/*
 * An XML document type.
 * Localname: notes
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one notes(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class NotesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument {
    private static final long serialVersionUID = 1L;

    public NotesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "notes"),
    };


    /**
     * Gets the "notes" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide getNotes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "notes" element
     */
    @Override
    public void setNotes(org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide notes) {
        generatedSetterHelperImpl(notes, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "notes" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide addNewNotes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
