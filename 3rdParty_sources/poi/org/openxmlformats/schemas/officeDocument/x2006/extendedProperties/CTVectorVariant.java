/*
 * XML Type:  CT_VectorVariant
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/extended-properties
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.extendedProperties;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_VectorVariant(@http://schemas.openxmlformats.org/officeDocument/2006/extended-properties).
 *
 * This is a complex type.
 */
public interface CTVectorVariant extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctvectorvariant9d75type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "vector" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector getVector();

    /**
     * Sets the "vector" element
     */
    void setVector(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector vector);

    /**
     * Appends and returns a new empty "vector" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector addNewVector();
}
