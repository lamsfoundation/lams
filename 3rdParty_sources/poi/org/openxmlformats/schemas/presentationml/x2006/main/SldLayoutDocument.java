/*
 * An XML document type.
 * Localname: sldLayout
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.SldLayoutDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one sldLayout(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface SldLayoutDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.SldLayoutDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sldlayout638edoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sldLayout" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout getSldLayout();

    /**
     * Sets the "sldLayout" element
     */
    void setSldLayout(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout sldLayout);

    /**
     * Appends and returns a new empty "sldLayout" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout addNewSldLayout();
}
