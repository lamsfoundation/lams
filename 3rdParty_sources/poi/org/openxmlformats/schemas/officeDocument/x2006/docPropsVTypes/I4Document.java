/*
 * An XML document type.
 * Localname: i4
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.I4Document
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one i4(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface I4Document extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.I4Document> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "i4a2e6doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "i4" element
     */
    int getI4();

    /**
     * Gets (as xml) the "i4" element
     */
    org.apache.xmlbeans.XmlInt xgetI4();

    /**
     * Sets the "i4" element
     */
    void setI4(int i4);

    /**
     * Sets (as xml) the "i4" element
     */
    void xsetI4(org.apache.xmlbeans.XmlInt i4);
}
