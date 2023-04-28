/*
 * An XML document type.
 * Localname: cy
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CyDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one cy(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface CyDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CyDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cy7a3bdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cy" element
     */
    java.lang.String getCy();

    /**
     * Gets (as xml) the "cy" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STCy xgetCy();

    /**
     * Sets the "cy" element
     */
    void setCy(java.lang.String cy);

    /**
     * Sets (as xml) the "cy" element
     */
    void xsetCy(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STCy cy);
}
