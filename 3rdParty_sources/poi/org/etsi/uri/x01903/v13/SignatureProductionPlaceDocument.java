/*
 * An XML document type.
 * Localname: SignatureProductionPlace
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignatureProductionPlaceDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SignatureProductionPlace(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface SignatureProductionPlaceDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignatureProductionPlaceDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signatureproductionplace2b3adoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignatureProductionPlace" element
     */
    org.etsi.uri.x01903.v13.SignatureProductionPlaceType getSignatureProductionPlace();

    /**
     * Sets the "SignatureProductionPlace" element
     */
    void setSignatureProductionPlace(org.etsi.uri.x01903.v13.SignatureProductionPlaceType signatureProductionPlace);

    /**
     * Appends and returns a new empty "SignatureProductionPlace" element
     */
    org.etsi.uri.x01903.v13.SignatureProductionPlaceType addNewSignatureProductionPlace();
}
