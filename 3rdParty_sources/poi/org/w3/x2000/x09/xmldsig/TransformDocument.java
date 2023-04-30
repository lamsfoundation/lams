/*
 * An XML document type.
 * Localname: Transform
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.TransformDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one Transform(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface TransformDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.TransformDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "transforme335doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Transform" element
     */
    org.w3.x2000.x09.xmldsig.TransformType getTransform();

    /**
     * Sets the "Transform" element
     */
    void setTransform(org.w3.x2000.x09.xmldsig.TransformType transform);

    /**
     * Appends and returns a new empty "Transform" element
     */
    org.w3.x2000.x09.xmldsig.TransformType addNewTransform();
}
