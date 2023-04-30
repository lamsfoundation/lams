/*
 * An XML document type.
 * Localname: XAdESTimeStamp
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.XAdESTimeStampDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one XAdESTimeStamp(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface XAdESTimeStampDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.XAdESTimeStampDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "xadestimestamp5b11doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "XAdESTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getXAdESTimeStamp();

    /**
     * Sets the "XAdESTimeStamp" element
     */
    void setXAdESTimeStamp(org.etsi.uri.x01903.v13.XAdESTimeStampType xAdESTimeStamp);

    /**
     * Appends and returns a new empty "XAdESTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewXAdESTimeStamp();
}
