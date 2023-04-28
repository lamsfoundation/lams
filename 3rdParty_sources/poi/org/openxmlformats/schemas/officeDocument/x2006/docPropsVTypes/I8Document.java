/*
 * An XML document type.
 * Localname: i8
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.I8Document
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one i8(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface I8Document extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.I8Document> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "i82562doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "i8" element
     */
    long getI8();

    /**
     * Gets (as xml) the "i8" element
     */
    org.apache.xmlbeans.XmlLong xgetI8();

    /**
     * Sets the "i8" element
     */
    void setI8(long i8);

    /**
     * Sets (as xml) the "i8" element
     */
    void xsetI8(org.apache.xmlbeans.XmlLong i8);
}
