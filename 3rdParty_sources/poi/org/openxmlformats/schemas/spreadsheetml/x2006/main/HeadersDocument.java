/*
 * An XML document type.
 * Localname: headers
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.HeadersDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one headers(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface HeadersDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.HeadersDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "headers2b91doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "headers" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionHeaders getHeaders();

    /**
     * Sets the "headers" element
     */
    void setHeaders(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionHeaders headers);

    /**
     * Appends and returns a new empty "headers" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionHeaders addNewHeaders();
}
