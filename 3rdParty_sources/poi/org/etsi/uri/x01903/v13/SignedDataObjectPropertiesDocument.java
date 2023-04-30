/*
 * An XML document type.
 * Localname: SignedDataObjectProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignedDataObjectPropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SignedDataObjectProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface SignedDataObjectPropertiesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignedDataObjectPropertiesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signeddataobjectproperties4b5cdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignedDataObjectProperties" element
     */
    org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType getSignedDataObjectProperties();

    /**
     * Sets the "SignedDataObjectProperties" element
     */
    void setSignedDataObjectProperties(org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType signedDataObjectProperties);

    /**
     * Appends and returns a new empty "SignedDataObjectProperties" element
     */
    org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType addNewSignedDataObjectProperties();
}
