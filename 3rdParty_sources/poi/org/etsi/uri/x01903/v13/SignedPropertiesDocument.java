/*
 * An XML document type.
 * Localname: SignedProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignedPropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SignedProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface SignedPropertiesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignedPropertiesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signedproperties7f73doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignedProperties" element
     */
    org.etsi.uri.x01903.v13.SignedPropertiesType getSignedProperties();

    /**
     * Sets the "SignedProperties" element
     */
    void setSignedProperties(org.etsi.uri.x01903.v13.SignedPropertiesType signedProperties);

    /**
     * Appends and returns a new empty "SignedProperties" element
     */
    org.etsi.uri.x01903.v13.SignedPropertiesType addNewSignedProperties();
}
