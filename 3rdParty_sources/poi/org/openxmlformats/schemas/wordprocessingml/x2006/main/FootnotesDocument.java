/*
 * An XML document type.
 * Localname: footnotes
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one footnotes(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface FootnotesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "footnotes8773doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "footnotes" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes getFootnotes();

    /**
     * Sets the "footnotes" element
     */
    void setFootnotes(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes footnotes);

    /**
     * Appends and returns a new empty "footnotes" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes addNewFootnotes();
}
