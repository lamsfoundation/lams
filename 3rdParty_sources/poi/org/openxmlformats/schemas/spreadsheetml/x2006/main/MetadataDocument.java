/*
 * An XML document type.
 * Localname: metadata
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.MetadataDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one metadata(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface MetadataDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.MetadataDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "metadatad98cdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "metadata" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadata getMetadata();

    /**
     * Sets the "metadata" element
     */
    void setMetadata(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadata metadata);

    /**
     * Appends and returns a new empty "metadata" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadata addNewMetadata();
}
