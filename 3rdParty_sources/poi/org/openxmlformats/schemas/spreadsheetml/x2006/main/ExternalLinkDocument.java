/*
 * An XML document type.
 * Localname: externalLink
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.ExternalLinkDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one externalLink(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface ExternalLinkDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.ExternalLinkDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "externallinkb4c2doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "externalLink" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink getExternalLink();

    /**
     * Sets the "externalLink" element
     */
    void setExternalLink(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink externalLink);

    /**
     * Appends and returns a new empty "externalLink" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink addNewExternalLink();
}
