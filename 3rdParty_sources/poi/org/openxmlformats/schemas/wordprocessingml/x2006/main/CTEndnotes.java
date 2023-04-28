/*
 * XML Type:  CT_Endnotes
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Endnotes(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTEndnotes extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctendnotescee2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "endnote" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn> getEndnoteList();

    /**
     * Gets array of all "endnote" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn[] getEndnoteArray();

    /**
     * Gets ith "endnote" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn getEndnoteArray(int i);

    /**
     * Returns number of "endnote" element
     */
    int sizeOfEndnoteArray();

    /**
     * Sets array of all "endnote" element
     */
    void setEndnoteArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn[] endnoteArray);

    /**
     * Sets ith "endnote" element
     */
    void setEndnoteArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn endnote);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "endnote" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn insertNewEndnote(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "endnote" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn addNewEndnote();

    /**
     * Removes the ith "endnote" element
     */
    void removeEndnote(int i);
}
