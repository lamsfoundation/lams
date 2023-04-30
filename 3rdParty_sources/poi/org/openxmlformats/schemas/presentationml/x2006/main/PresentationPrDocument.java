/*
 * An XML document type.
 * Localname: presentationPr
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.PresentationPrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one presentationPr(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface PresentationPrDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.PresentationPrDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "presentationpr6c55doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "presentationPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTPresentationProperties getPresentationPr();

    /**
     * Sets the "presentationPr" element
     */
    void setPresentationPr(org.openxmlformats.schemas.presentationml.x2006.main.CTPresentationProperties presentationPr);

    /**
     * Appends and returns a new empty "presentationPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTPresentationProperties addNewPresentationPr();
}
