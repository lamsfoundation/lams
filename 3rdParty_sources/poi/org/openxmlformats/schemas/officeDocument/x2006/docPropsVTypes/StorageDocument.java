/*
 * An XML document type.
 * Localname: storage
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.StorageDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one storage(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface StorageDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.StorageDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "storage44e8doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "storage" element
     */
    byte[] getStorage();

    /**
     * Gets (as xml) the "storage" element
     */
    org.apache.xmlbeans.XmlBase64Binary xgetStorage();

    /**
     * Sets the "storage" element
     */
    void setStorage(byte[] storage);

    /**
     * Sets (as xml) the "storage" element
     */
    void xsetStorage(org.apache.xmlbeans.XmlBase64Binary storage);
}
