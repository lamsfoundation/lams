/*
 * An XML document type.
 * Localname: clsid
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.ClsidDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one clsid(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface ClsidDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.ClsidDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "clsid941edoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "clsid" element
     */
    java.lang.String getClsid();

    /**
     * Gets (as xml) the "clsid" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetClsid();

    /**
     * Sets the "clsid" element
     */
    void setClsid(java.lang.String clsid);

    /**
     * Sets (as xml) the "clsid" element
     */
    void xsetClsid(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid clsid);
}
