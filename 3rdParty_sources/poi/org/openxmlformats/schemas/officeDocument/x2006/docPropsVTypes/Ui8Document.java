/*
 * An XML document type.
 * Localname: ui8
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.Ui8Document
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one ui8(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface Ui8Document extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.Ui8Document> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ui8915fdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ui8" element
     */
    java.math.BigInteger getUi8();

    /**
     * Gets (as xml) the "ui8" element
     */
    org.apache.xmlbeans.XmlUnsignedLong xgetUi8();

    /**
     * Sets the "ui8" element
     */
    void setUi8(java.math.BigInteger ui8);

    /**
     * Sets (as xml) the "ui8" element
     */
    void xsetUi8(org.apache.xmlbeans.XmlUnsignedLong ui8);
}
