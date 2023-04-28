/*
 * An XML document type.
 * Localname: null
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.NullDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one null(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface NullDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.NullDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "null0e0adoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "null" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTNull getNull();

    /**
     * Sets the "null" element
     */
    void setNull(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTNull xnull);

    /**
     * Appends and returns a new empty "null" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTNull addNewNull();
}
