/*
 * An XML document type.
 * Localname: notesMaster
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.NotesMasterDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one notesMaster(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface NotesMasterDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.NotesMasterDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "notesmaster8840doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "notesMaster" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMaster getNotesMaster();

    /**
     * Sets the "notesMaster" element
     */
    void setNotesMaster(org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMaster notesMaster);

    /**
     * Appends and returns a new empty "notesMaster" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMaster addNewNotesMaster();
}
