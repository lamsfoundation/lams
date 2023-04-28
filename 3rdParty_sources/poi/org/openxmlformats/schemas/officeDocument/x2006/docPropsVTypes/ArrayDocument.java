/*
 * An XML document type.
 * Localname: array
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.ArrayDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one array(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface ArrayDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.ArrayDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "arrayaa0adoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "array" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTArray getArray();

    /**
     * Sets the "array" element
     */
    void setArray(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTArray array);

    /**
     * Appends and returns a new empty "array" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTArray addNewArray();
}
