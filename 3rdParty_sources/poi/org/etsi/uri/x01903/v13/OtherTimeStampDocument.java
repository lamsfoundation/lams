/*
 * An XML document type.
 * Localname: OtherTimeStamp
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OtherTimeStampDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one OtherTimeStamp(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface OtherTimeStampDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.OtherTimeStampDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "othertimestamp924adoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "OtherTimeStamp" element
     */
    org.etsi.uri.x01903.v13.OtherTimeStampType getOtherTimeStamp();

    /**
     * Sets the "OtherTimeStamp" element
     */
    void setOtherTimeStamp(org.etsi.uri.x01903.v13.OtherTimeStampType otherTimeStamp);

    /**
     * Appends and returns a new empty "OtherTimeStamp" element
     */
    org.etsi.uri.x01903.v13.OtherTimeStampType addNewOtherTimeStamp();
}
