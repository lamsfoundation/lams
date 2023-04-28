/*
 * An XML document type.
 * Localname: notes
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one notes(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface NotesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "notes4a02doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "notes" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide getNotes();

    /**
     * Sets the "notes" element
     */
    void setNotes(org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide notes);

    /**
     * Appends and returns a new empty "notes" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide addNewNotes();
}
