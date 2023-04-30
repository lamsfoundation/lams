/*
 * An XML document type.
 * Localname: notesMaster
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.NotesMasterDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one notesMaster(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class NotesMasterDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.NotesMasterDocument {
    private static final long serialVersionUID = 1L;

    public NotesMasterDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "notesMaster"),
    };


    /**
     * Gets the "notesMaster" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMaster getNotesMaster() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMaster target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMaster)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "notesMaster" element
     */
    @Override
    public void setNotesMaster(org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMaster notesMaster) {
        generatedSetterHelperImpl(notesMaster, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "notesMaster" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMaster addNewNotesMaster() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMaster target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMaster)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
