/*
 * XML Type:  CounterSignatureType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CounterSignatureType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CounterSignatureType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CounterSignatureType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CounterSignatureType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "countersignaturetypee52atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Signature" element
     */
    org.w3.x2000.x09.xmldsig.SignatureType getSignature();

    /**
     * Sets the "Signature" element
     */
    void setSignature(org.w3.x2000.x09.xmldsig.SignatureType signature);

    /**
     * Appends and returns a new empty "Signature" element
     */
    org.w3.x2000.x09.xmldsig.SignatureType addNewSignature();
}
