/*
 * An XML document type.
 * Localname: decimal
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.DecimalDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one decimal(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface DecimalDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.DecimalDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "decimal39d2doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "decimal" element
     */
    java.math.BigDecimal getDecimal();

    /**
     * Gets (as xml) the "decimal" element
     */
    org.apache.xmlbeans.XmlDecimal xgetDecimal();

    /**
     * Sets the "decimal" element
     */
    void setDecimal(java.math.BigDecimal decimal);

    /**
     * Sets (as xml) the "decimal" element
     */
    void xsetDecimal(org.apache.xmlbeans.XmlDecimal decimal);
}
