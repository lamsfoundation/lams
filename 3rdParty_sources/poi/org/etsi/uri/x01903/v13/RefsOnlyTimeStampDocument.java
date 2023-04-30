/*
 * An XML document type.
 * Localname: RefsOnlyTimeStamp
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.RefsOnlyTimeStampDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one RefsOnlyTimeStamp(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface RefsOnlyTimeStampDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.RefsOnlyTimeStampDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "refsonlytimestampddfcdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "RefsOnlyTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getRefsOnlyTimeStamp();

    /**
     * Sets the "RefsOnlyTimeStamp" element
     */
    void setRefsOnlyTimeStamp(org.etsi.uri.x01903.v13.XAdESTimeStampType refsOnlyTimeStamp);

    /**
     * Appends and returns a new empty "RefsOnlyTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewRefsOnlyTimeStamp();
}
