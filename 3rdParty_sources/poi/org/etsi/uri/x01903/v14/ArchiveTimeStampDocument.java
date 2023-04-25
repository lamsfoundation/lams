/*
 * An XML document type.
 * Localname: ArchiveTimeStamp
 * Namespace: http://uri.etsi.org/01903/v1.4.1#
 * Java type: org.etsi.uri.x01903.v14.ArchiveTimeStampDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v14;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one ArchiveTimeStamp(@http://uri.etsi.org/01903/v1.4.1#) element.
 *
 * This is a complex type.
 */
public interface ArchiveTimeStampDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v14.ArchiveTimeStampDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "archivetimestamp0758doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ArchiveTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getArchiveTimeStamp();

    /**
     * Sets the "ArchiveTimeStamp" element
     */
    void setArchiveTimeStamp(org.etsi.uri.x01903.v13.XAdESTimeStampType archiveTimeStamp);

    /**
     * Appends and returns a new empty "ArchiveTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewArchiveTimeStamp();
}
