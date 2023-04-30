/*
 * An XML document type.
 * Localname: fonts
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.FontsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one fonts(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface FontsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.FontsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "fonts7aa2doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fonts" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList getFonts();

    /**
     * Sets the "fonts" element
     */
    void setFonts(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList fonts);

    /**
     * Appends and returns a new empty "fonts" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList addNewFonts();
}
