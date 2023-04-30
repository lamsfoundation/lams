/*
 * An XML document type.
 * Localname: UnsignedSignatureProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one UnsignedSignatureProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface UnsignedSignaturePropertiesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "unsignedsignatureproperties5cc8doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "UnsignedSignatureProperties" element
     */
    org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType getUnsignedSignatureProperties();

    /**
     * Sets the "UnsignedSignatureProperties" element
     */
    void setUnsignedSignatureProperties(org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType unsignedSignatureProperties);

    /**
     * Appends and returns a new empty "UnsignedSignatureProperties" element
     */
    org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType addNewUnsignedSignatureProperties();
}
