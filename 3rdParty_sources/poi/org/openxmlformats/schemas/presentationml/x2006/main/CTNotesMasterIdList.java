/*
 * XML Type:  CT_NotesMasterIdList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NotesMasterIdList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNotesMasterIdList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnotesmasteridlist2853type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "notesMasterId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdListEntry getNotesMasterId();

    /**
     * True if has "notesMasterId" element
     */
    boolean isSetNotesMasterId();

    /**
     * Sets the "notesMasterId" element
     */
    void setNotesMasterId(org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdListEntry notesMasterId);

    /**
     * Appends and returns a new empty "notesMasterId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdListEntry addNewNotesMasterId();

    /**
     * Unsets the "notesMasterId" element
     */
    void unsetNotesMasterId();
}
