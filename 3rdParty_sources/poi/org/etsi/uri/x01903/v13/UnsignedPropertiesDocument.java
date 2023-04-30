/*
 * An XML document type.
 * Localname: UnsignedProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.UnsignedPropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one UnsignedProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface UnsignedPropertiesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.UnsignedPropertiesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "unsignedproperties238cdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "UnsignedProperties" element
     */
    org.etsi.uri.x01903.v13.UnsignedPropertiesType getUnsignedProperties();

    /**
     * Sets the "UnsignedProperties" element
     */
    void setUnsignedProperties(org.etsi.uri.x01903.v13.UnsignedPropertiesType unsignedProperties);

    /**
     * Appends and returns a new empty "UnsignedProperties" element
     */
    org.etsi.uri.x01903.v13.UnsignedPropertiesType addNewUnsignedProperties();
}
