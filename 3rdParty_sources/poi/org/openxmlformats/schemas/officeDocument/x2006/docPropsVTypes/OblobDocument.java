/*
 * An XML document type.
 * Localname: oblob
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.OblobDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one oblob(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface OblobDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.OblobDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "oblobbbb7doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "oblob" element
     */
    byte[] getOblob();

    /**
     * Gets (as xml) the "oblob" element
     */
    org.apache.xmlbeans.XmlBase64Binary xgetOblob();

    /**
     * Sets the "oblob" element
     */
    void setOblob(byte[] oblob);

    /**
     * Sets (as xml) the "oblob" element
     */
    void xsetOblob(org.apache.xmlbeans.XmlBase64Binary oblob);
}
