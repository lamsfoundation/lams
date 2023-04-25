/*
 * An XML document type.
 * Localname: document
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.DocumentDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one document(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface DocumentDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.DocumentDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "document2bd9doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "document" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1 getDocument();

    /**
     * Sets the "document" element
     */
    void setDocument(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1 document);

    /**
     * Appends and returns a new empty "document" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1 addNewDocument();
}
