/*
 * An XML document type.
 * Localname: vstream
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.VstreamDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one vstream(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface VstreamDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.VstreamDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "vstream24cddoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "vstream" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream getVstream();

    /**
     * Sets the "vstream" element
     */
    void setVstream(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream vstream);

    /**
     * Appends and returns a new empty "vstream" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream addNewVstream();
}
