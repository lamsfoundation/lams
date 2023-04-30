/*
 * An XML document type.
 * Localname: int
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.IntDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one int(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface IntDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.IntDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "int3c74doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "int" element
     */
    int getInt();

    /**
     * Gets (as xml) the "int" element
     */
    org.apache.xmlbeans.XmlInt xgetInt();

    /**
     * Sets the "int" element
     */
    void setInt(int xint);

    /**
     * Sets (as xml) the "int" element
     */
    void xsetInt(org.apache.xmlbeans.XmlInt xint);
}
