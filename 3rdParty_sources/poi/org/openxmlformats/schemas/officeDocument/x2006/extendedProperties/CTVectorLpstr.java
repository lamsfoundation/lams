/*
 * XML Type:  CT_VectorLpstr
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/extended-properties
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorLpstr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.extendedProperties;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_VectorLpstr(@http://schemas.openxmlformats.org/officeDocument/2006/extended-properties).
 *
 * This is a complex type.
 */
public interface CTVectorLpstr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorLpstr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctvectorlpstr9b1dtype");
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
