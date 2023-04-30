/*
 * An XML document type.
 * Localname: presentation
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.PresentationDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one presentation(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface PresentationDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.PresentationDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "presentation02f7doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "presentation" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation getPresentation();

    /**
     * Sets the "presentation" element
     */
    void setPresentation(org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation presentation);

    /**
     * Appends and returns a new empty "presentation" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation addNewPresentation();
}
