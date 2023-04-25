/*
 * An XML document type.
 * Localname: uint
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.UintDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one uint(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface UintDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.UintDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "uint5017doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "uint" element
     */
    long getUint();

    /**
     * Gets (as xml) the "uint" element
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetUint();

    /**
     * Sets the "uint" element
     */
    void setUint(long uint);

    /**
     * Sets (as xml) the "uint" element
     */
    void xsetUint(org.apache.xmlbeans.XmlUnsignedInt uint);
}
