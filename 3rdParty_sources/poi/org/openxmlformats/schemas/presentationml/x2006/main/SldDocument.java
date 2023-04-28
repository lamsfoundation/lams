/*
 * An XML document type.
 * Localname: sld
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.SldDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one sld(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface SldDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.SldDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sld1b98doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sld" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlide getSld();

    /**
     * Sets the "sld" element
     */
    void setSld(org.openxmlformats.schemas.presentationml.x2006.main.CTSlide sld);

    /**
     * Appends and returns a new empty "sld" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlide addNewSld();
}
