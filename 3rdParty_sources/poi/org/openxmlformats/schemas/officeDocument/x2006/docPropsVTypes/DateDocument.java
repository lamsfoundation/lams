/*
 * An XML document type.
 * Localname: date
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.DateDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one date(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface DateDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.DateDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "datedc43doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "date" element
     */
    java.util.Calendar getDate();

    /**
     * Gets (as xml) the "date" element
     */
    org.apache.xmlbeans.XmlDateTime xgetDate();

    /**
     * Sets the "date" element
     */
    void setDate(java.util.Calendar date);

    /**
     * Sets (as xml) the "date" element
     */
    void xsetDate(org.apache.xmlbeans.XmlDateTime date);
}
