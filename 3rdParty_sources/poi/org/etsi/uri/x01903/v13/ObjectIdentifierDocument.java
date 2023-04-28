/*
 * An XML document type.
 * Localname: ObjectIdentifier
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.ObjectIdentifierDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one ObjectIdentifier(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface ObjectIdentifierDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.ObjectIdentifierDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "objectidentifier0d0cdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ObjectIdentifier" element
     */
    org.etsi.uri.x01903.v13.ObjectIdentifierType getObjectIdentifier();

    /**
     * Sets the "ObjectIdentifier" element
     */
    void setObjectIdentifier(org.etsi.uri.x01903.v13.ObjectIdentifierType objectIdentifier);

    /**
     * Appends and returns a new empty "ObjectIdentifier" element
     */
    org.etsi.uri.x01903.v13.ObjectIdentifierType addNewObjectIdentifier();
}
