/*
 * An XML document type.
 * Localname: SigAndRefsTimeStamp
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SigAndRefsTimeStampDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SigAndRefsTimeStamp(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface SigAndRefsTimeStampDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SigAndRefsTimeStampDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sigandrefstimestamp7762doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SigAndRefsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getSigAndRefsTimeStamp();

    /**
     * Sets the "SigAndRefsTimeStamp" element
     */
    void setSigAndRefsTimeStamp(org.etsi.uri.x01903.v13.XAdESTimeStampType sigAndRefsTimeStamp);

    /**
     * Appends and returns a new empty "SigAndRefsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewSigAndRefsTimeStamp();
}
