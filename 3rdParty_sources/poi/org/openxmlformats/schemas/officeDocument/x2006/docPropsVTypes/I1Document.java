/*
 * An XML document type.
 * Localname: i1
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.I1Document
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one i1(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface I1Document extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.I1Document> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "i18109doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "i1" element
     */
    byte getI1();

    /**
     * Gets (as xml) the "i1" element
     */
    org.apache.xmlbeans.XmlByte xgetI1();

    /**
     * Sets the "i1" element
     */
    void setI1(byte i1);

    /**
     * Sets (as xml) the "i1" element
     */
    void xsetI1(org.apache.xmlbeans.XmlByte i1);
}
