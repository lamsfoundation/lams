/*
 * An XML document type.
 * Localname: endnotes
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.EndnotesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one endnotes(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface EndnotesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.EndnotesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "endnotes960edoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "endnotes" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes getEndnotes();

    /**
     * Sets the "endnotes" element
     */
    void setEndnotes(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes endnotes);

    /**
     * Appends and returns a new empty "endnotes" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes addNewEndnotes();
}
