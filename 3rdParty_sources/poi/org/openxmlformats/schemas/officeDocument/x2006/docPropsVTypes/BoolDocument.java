/*
 * An XML document type.
 * Localname: bool
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.BoolDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one bool(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface BoolDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.BoolDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "bool1687doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bool" element
     */
    boolean getBool();

    /**
     * Gets (as xml) the "bool" element
     */
    org.apache.xmlbeans.XmlBoolean xgetBool();

    /**
     * Sets the "bool" element
     */
    void setBool(boolean bool);

    /**
     * Sets (as xml) the "bool" element
     */
    void xsetBool(org.apache.xmlbeans.XmlBoolean bool);
}
