/*
 * An XML document type.
 * Localname: TimeStampValidationData
 * Namespace: http://uri.etsi.org/01903/v1.4.1#
 * Java type: org.etsi.uri.x01903.v14.TimeStampValidationDataDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v14;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one TimeStampValidationData(@http://uri.etsi.org/01903/v1.4.1#) element.
 *
 * This is a complex type.
 */
public interface TimeStampValidationDataDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v14.TimeStampValidationDataDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "timestampvalidationdataeb4bdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "TimeStampValidationData" element
     */
    org.etsi.uri.x01903.v14.ValidationDataType getTimeStampValidationData();

    /**
     * Sets the "TimeStampValidationData" element
     */
    void setTimeStampValidationData(org.etsi.uri.x01903.v14.ValidationDataType timeStampValidationData);

    /**
     * Appends and returns a new empty "TimeStampValidationData" element
     */
    org.etsi.uri.x01903.v14.ValidationDataType addNewTimeStampValidationData();
}
