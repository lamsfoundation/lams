/*
 * An XML document type.
 * Localname: CounterSignature
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CounterSignatureDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one CounterSignature(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface CounterSignatureDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CounterSignatureDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "countersignaturecce0doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "CounterSignature" element
     */
    org.etsi.uri.x01903.v13.CounterSignatureType getCounterSignature();

    /**
     * Sets the "CounterSignature" element
     */
    void setCounterSignature(org.etsi.uri.x01903.v13.CounterSignatureType counterSignature);

    /**
     * Appends and returns a new empty "CounterSignature" element
     */
    org.etsi.uri.x01903.v13.CounterSignatureType addNewCounterSignature();
}
