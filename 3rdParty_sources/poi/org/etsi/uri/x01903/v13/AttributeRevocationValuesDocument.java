/*
 * An XML document type.
 * Localname: AttributeRevocationValues
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.AttributeRevocationValuesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one AttributeRevocationValues(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface AttributeRevocationValuesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.AttributeRevocationValuesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "attributerevocationvalues774edoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "AttributeRevocationValues" element
     */
    org.etsi.uri.x01903.v13.RevocationValuesType getAttributeRevocationValues();

    /**
     * Sets the "AttributeRevocationValues" element
     */
    void setAttributeRevocationValues(org.etsi.uri.x01903.v13.RevocationValuesType attributeRevocationValues);

    /**
     * Appends and returns a new empty "AttributeRevocationValues" element
     */
    org.etsi.uri.x01903.v13.RevocationValuesType addNewAttributeRevocationValues();
}
