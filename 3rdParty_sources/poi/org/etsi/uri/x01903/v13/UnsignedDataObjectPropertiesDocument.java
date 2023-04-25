/*
 * An XML document type.
 * Localname: UnsignedDataObjectProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one UnsignedDataObjectProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface UnsignedDataObjectPropertiesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "unsigneddataobjectproperties6435doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "UnsignedDataObjectProperties" element
     */
    org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType getUnsignedDataObjectProperties();

    /**
     * Sets the "UnsignedDataObjectProperties" element
     */
    void setUnsignedDataObjectProperties(org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType unsignedDataObjectProperties);

    /**
     * Appends and returns a new empty "UnsignedDataObjectProperties" element
     */
    org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType addNewUnsignedDataObjectProperties();
}
