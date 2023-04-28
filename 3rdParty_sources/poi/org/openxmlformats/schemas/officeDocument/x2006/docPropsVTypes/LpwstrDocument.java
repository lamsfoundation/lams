/*
 * An XML document type.
 * Localname: lpwstr
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.LpwstrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one lpwstr(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface LpwstrDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.LpwstrDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "lpwstr5a53doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "lpwstr" element
     */
    java.lang.String getLpwstr();

    /**
     * Gets (as xml) the "lpwstr" element
     */
    org.apache.xmlbeans.XmlString xgetLpwstr();

    /**
     * Sets the "lpwstr" element
     */
    void setLpwstr(java.lang.String lpwstr);

    /**
     * Sets (as xml) the "lpwstr" element
     */
    void xsetLpwstr(org.apache.xmlbeans.XmlString lpwstr);
}
