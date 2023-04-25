/*
 * An XML document type.
 * Localname: comments
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CommentsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one comments(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface CommentsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CommentsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "comments4c11doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "comments" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments getComments();

    /**
     * Sets the "comments" element
     */
    void setComments(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments comments);

    /**
     * Appends and returns a new empty "comments" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments addNewComments();
}
