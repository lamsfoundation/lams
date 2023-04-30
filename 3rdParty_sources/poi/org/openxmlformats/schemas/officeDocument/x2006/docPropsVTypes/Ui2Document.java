/*
 * An XML document type.
 * Localname: ui2
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.Ui2Document
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one ui2(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface Ui2Document extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.Ui2Document> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ui24da5doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ui2" element
     */
    int getUi2();

    /**
     * Gets (as xml) the "ui2" element
     */
    org.apache.xmlbeans.XmlUnsignedShort xgetUi2();

    /**
     * Sets the "ui2" element
     */
    void setUi2(int ui2);

    /**
     * Sets (as xml) the "ui2" element
     */
    void xsetUi2(org.apache.xmlbeans.XmlUnsignedShort ui2);
}
