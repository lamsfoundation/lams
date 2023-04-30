/*
 * An XML document type.
 * Localname: Transforms
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.TransformsDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one Transforms(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface TransformsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.TransformsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "transformsab28doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Transforms" element
     */
    org.w3.x2000.x09.xmldsig.TransformsType getTransforms();

    /**
     * Sets the "Transforms" element
     */
    void setTransforms(org.w3.x2000.x09.xmldsig.TransformsType transforms);

    /**
     * Appends and returns a new empty "Transforms" element
     */
    org.w3.x2000.x09.xmldsig.TransformsType addNewTransforms();
}
