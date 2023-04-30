/*
 * An XML document type.
 * Localname: Sources
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.SourcesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one Sources(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography) element.
 *
 * This is a complex type.
 */
public interface SourcesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.bibliography.SourcesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sources5a72doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Sources" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources getSources();

    /**
     * Sets the "Sources" element
     */
    void setSources(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources sources);

    /**
     * Appends and returns a new empty "Sources" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources addNewSources();
}
