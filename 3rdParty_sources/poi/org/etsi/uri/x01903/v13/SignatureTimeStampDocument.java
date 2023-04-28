/*
 * An XML document type.
 * Localname: SignatureTimeStamp
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignatureTimeStampDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SignatureTimeStamp(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface SignatureTimeStampDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignatureTimeStampDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signaturetimestamp0b22doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignatureTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getSignatureTimeStamp();

    /**
     * Sets the "SignatureTimeStamp" element
     */
    void setSignatureTimeStamp(org.etsi.uri.x01903.v13.XAdESTimeStampType signatureTimeStamp);

    /**
     * Appends and returns a new empty "SignatureTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewSignatureTimeStamp();
}
