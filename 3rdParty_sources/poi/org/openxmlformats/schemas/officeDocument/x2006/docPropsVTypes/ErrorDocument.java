/*
 * An XML document type.
 * Localname: error
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.ErrorDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one error(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface ErrorDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.ErrorDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "errorb7bbdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "error" element
     */
    java.lang.String getError();

    /**
     * Gets (as xml) the "error" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STError xgetError();

    /**
     * Sets the "error" element
     */
    void setError(java.lang.String error);

    /**
     * Sets (as xml) the "error" element
     */
    void xsetError(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STError error);
}
