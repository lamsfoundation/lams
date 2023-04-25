/*
 * An XML document type.
 * Localname: ostorage
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.OstorageDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one ostorage(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface OstorageDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.OstorageDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ostoraged805doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ostorage" element
     */
    byte[] getOstorage();

    /**
     * Gets (as xml) the "ostorage" element
     */
    org.apache.xmlbeans.XmlBase64Binary xgetOstorage();

    /**
     * Sets the "ostorage" element
     */
    void setOstorage(byte[] ostorage);

    /**
     * Sets (as xml) the "ostorage" element
     */
    void xsetOstorage(org.apache.xmlbeans.XmlBase64Binary ostorage);
}
