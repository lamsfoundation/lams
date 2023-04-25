/*
 * An XML document type.
 * Localname: styles
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one styles(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface StylesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "styles2732doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "styles" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles getStyles();

    /**
     * Sets the "styles" element
     */
    void setStyles(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles styles);

    /**
     * Appends and returns a new empty "styles" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles addNewStyles();
}
