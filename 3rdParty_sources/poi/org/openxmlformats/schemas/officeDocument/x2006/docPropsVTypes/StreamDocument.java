/*
 * An XML document type.
 * Localname: stream
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.StreamDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one stream(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface StreamDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.StreamDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "streamd1f1doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "stream" element
     */
    byte[] getStream();

    /**
     * Gets (as xml) the "stream" element
     */
    org.apache.xmlbeans.XmlBase64Binary xgetStream();

    /**
     * Sets the "stream" element
     */
    void setStream(byte[] stream);

    /**
     * Sets (as xml) the "stream" element
     */
    void xsetStream(org.apache.xmlbeans.XmlBase64Binary stream);
}
