/*
 * An XML document type.
 * Localname: SigningTime
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SigningTimeDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SigningTime(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface SigningTimeDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SigningTimeDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signingtime9e64doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SigningTime" element
     */
    java.util.Calendar getSigningTime();

    /**
     * Gets (as xml) the "SigningTime" element
     */
    org.apache.xmlbeans.XmlDateTime xgetSigningTime();

    /**
     * Sets the "SigningTime" element
     */
    void setSigningTime(java.util.Calendar signingTime);

    /**
     * Sets (as xml) the "SigningTime" element
     */
    void xsetSigningTime(org.apache.xmlbeans.XmlDateTime signingTime);
}
