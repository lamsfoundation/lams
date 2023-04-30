/*
 * An XML document type.
 * Localname: IndividualDataObjectsTimeStamp
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.IndividualDataObjectsTimeStampDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one IndividualDataObjectsTimeStamp(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface IndividualDataObjectsTimeStampDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.IndividualDataObjectsTimeStampDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "individualdataobjectstimestamp4f09doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "IndividualDataObjectsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getIndividualDataObjectsTimeStamp();

    /**
     * Sets the "IndividualDataObjectsTimeStamp" element
     */
    void setIndividualDataObjectsTimeStamp(org.etsi.uri.x01903.v13.XAdESTimeStampType individualDataObjectsTimeStamp);

    /**
     * Appends and returns a new empty "IndividualDataObjectsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewIndividualDataObjectsTimeStamp();
}
