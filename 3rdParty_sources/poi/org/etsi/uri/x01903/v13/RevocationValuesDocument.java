/*
 * An XML document type.
 * Localname: RevocationValues
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.RevocationValuesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one RevocationValues(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface RevocationValuesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.RevocationValuesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "revocationvaluesc424doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "RevocationValues" element
     */
    org.etsi.uri.x01903.v13.RevocationValuesType getRevocationValues();

    /**
     * Sets the "RevocationValues" element
     */
    void setRevocationValues(org.etsi.uri.x01903.v13.RevocationValuesType revocationValues);

    /**
     * Appends and returns a new empty "RevocationValues" element
     */
    org.etsi.uri.x01903.v13.RevocationValuesType addNewRevocationValues();
}
