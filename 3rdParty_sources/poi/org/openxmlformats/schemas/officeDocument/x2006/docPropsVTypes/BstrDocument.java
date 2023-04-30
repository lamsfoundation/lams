/*
 * An XML document type.
 * Localname: bstr
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.BstrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one bstr(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface BstrDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.BstrDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "bstrae02doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bstr" element
     */
    java.lang.String getBstr();

    /**
     * Gets (as xml) the "bstr" element
     */
    org.apache.xmlbeans.XmlString xgetBstr();

    /**
     * Sets the "bstr" element
     */
    void setBstr(java.lang.String bstr);

    /**
     * Sets (as xml) the "bstr" element
     */
    void xsetBstr(org.apache.xmlbeans.XmlString bstr);
}
