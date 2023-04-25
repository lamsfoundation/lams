/*
 * An XML document type.
 * Localname: CertificateValues
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CertificateValuesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one CertificateValues(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface CertificateValuesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CertificateValuesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "certificatevalues2f8bdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "CertificateValues" element
     */
    org.etsi.uri.x01903.v13.CertificateValuesType getCertificateValues();

    /**
     * Sets the "CertificateValues" element
     */
    void setCertificateValues(org.etsi.uri.x01903.v13.CertificateValuesType certificateValues);

    /**
     * Appends and returns a new empty "CertificateValues" element
     */
    org.etsi.uri.x01903.v13.CertificateValuesType addNewCertificateValues();
}
