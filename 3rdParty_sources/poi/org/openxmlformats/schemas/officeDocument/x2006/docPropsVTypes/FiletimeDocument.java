/*
 * An XML document type.
 * Localname: filetime
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.FiletimeDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one filetime(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface FiletimeDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.FiletimeDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "filetime3708doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "filetime" element
     */
    java.util.Calendar getFiletime();

    /**
     * Gets (as xml) the "filetime" element
     */
    org.apache.xmlbeans.XmlDateTime xgetFiletime();

    /**
     * Sets the "filetime" element
     */
    void setFiletime(java.util.Calendar filetime);

    /**
     * Sets (as xml) the "filetime" element
     */
    void xsetFiletime(org.apache.xmlbeans.XmlDateTime filetime);
}
