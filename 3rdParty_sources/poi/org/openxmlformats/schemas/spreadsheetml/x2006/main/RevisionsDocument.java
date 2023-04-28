/*
 * An XML document type.
 * Localname: revisions
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.RevisionsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one revisions(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface RevisionsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.RevisionsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "revisions1fc3doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "revisions" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisions getRevisions();

    /**
     * Sets the "revisions" element
     */
    void setRevisions(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisions revisions);

    /**
     * Appends and returns a new empty "revisions" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisions addNewRevisions();
}
