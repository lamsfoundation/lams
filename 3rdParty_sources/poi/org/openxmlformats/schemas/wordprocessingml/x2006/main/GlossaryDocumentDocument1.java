/*
 * An XML document type.
 * Localname: glossaryDocument
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.GlossaryDocumentDocument1
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one glossaryDocument(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface GlossaryDocumentDocument1 extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.GlossaryDocumentDocument1> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "glossarydocument6d5bdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "glossaryDocument" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1 getGlossaryDocument();

    /**
     * Sets the "glossaryDocument" element
     */
    void setGlossaryDocument(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1 glossaryDocument);

    /**
     * Appends and returns a new empty "glossaryDocument" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1 addNewGlossaryDocument();
}
