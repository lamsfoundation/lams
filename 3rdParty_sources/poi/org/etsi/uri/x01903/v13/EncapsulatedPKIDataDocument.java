/*
 * An XML document type.
 * Localname: EncapsulatedPKIData
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.EncapsulatedPKIDataDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one EncapsulatedPKIData(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface EncapsulatedPKIDataDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.EncapsulatedPKIDataDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "encapsulatedpkidatabd97doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "EncapsulatedPKIData" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType getEncapsulatedPKIData();

    /**
     * Sets the "EncapsulatedPKIData" element
     */
    void setEncapsulatedPKIData(org.etsi.uri.x01903.v13.EncapsulatedPKIDataType encapsulatedPKIData);

    /**
     * Appends and returns a new empty "EncapsulatedPKIData" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType addNewEncapsulatedPKIData();
}
