/*
 * An XML document type.
 * Localname: DataObjectFormat
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.DataObjectFormatDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one DataObjectFormat(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface DataObjectFormatDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.DataObjectFormatDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "dataobjectformataea4doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "DataObjectFormat" element
     */
    org.etsi.uri.x01903.v13.DataObjectFormatType getDataObjectFormat();

    /**
     * Sets the "DataObjectFormat" element
     */
    void setDataObjectFormat(org.etsi.uri.x01903.v13.DataObjectFormatType dataObjectFormat);

    /**
     * Appends and returns a new empty "DataObjectFormat" element
     */
    org.etsi.uri.x01903.v13.DataObjectFormatType addNewDataObjectFormat();
}
