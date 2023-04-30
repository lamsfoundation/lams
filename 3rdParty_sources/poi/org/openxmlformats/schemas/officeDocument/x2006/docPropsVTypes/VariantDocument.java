/*
 * An XML document type.
 * Localname: variant
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.VariantDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one variant(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public interface VariantDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.VariantDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "variantfd1edoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "variant" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant getVariant();

    /**
     * Sets the "variant" element
     */
    void setVariant(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant variant);

    /**
     * Appends and returns a new empty "variant" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant addNewVariant();
}
