/*
 * An XML document type.
 * Localname: SignatureMethod
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignatureMethodDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SignatureMethod(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface SignatureMethodDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.SignatureMethodDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signaturemethode108doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignatureMethod" element
     */
    org.w3.x2000.x09.xmldsig.SignatureMethodType getSignatureMethod();

    /**
     * Sets the "SignatureMethod" element
     */
    void setSignatureMethod(org.w3.x2000.x09.xmldsig.SignatureMethodType signatureMethod);

    /**
     * Appends and returns a new empty "SignatureMethod" element
     */
    org.w3.x2000.x09.xmldsig.SignatureMethodType addNewSignatureMethod();
}
