/*
 * An XML document type.
 * Localname: AllDataObjectsTimeStamp
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.AllDataObjectsTimeStampDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one AllDataObjectsTimeStamp(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface AllDataObjectsTimeStampDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.AllDataObjectsTimeStampDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "alldataobjectstimestamp10ffdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "AllDataObjectsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getAllDataObjectsTimeStamp();

    /**
     * Sets the "AllDataObjectsTimeStamp" element
     */
    void setAllDataObjectsTimeStamp(org.etsi.uri.x01903.v13.XAdESTimeStampType allDataObjectsTimeStamp);

    /**
     * Appends and returns a new empty "AllDataObjectsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewAllDataObjectsTimeStamp();
}
