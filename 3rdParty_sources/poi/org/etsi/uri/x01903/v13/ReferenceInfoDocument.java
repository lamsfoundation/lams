/*
 * An XML document type.
 * Localname: ReferenceInfo
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.ReferenceInfoDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one ReferenceInfo(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface ReferenceInfoDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.ReferenceInfoDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "referenceinfoe80bdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ReferenceInfo" element
     */
    org.etsi.uri.x01903.v13.ReferenceInfoType getReferenceInfo();

    /**
     * Sets the "ReferenceInfo" element
     */
    void setReferenceInfo(org.etsi.uri.x01903.v13.ReferenceInfoType referenceInfo);

    /**
     * Appends and returns a new empty "ReferenceInfo" element
     */
    org.etsi.uri.x01903.v13.ReferenceInfoType addNewReferenceInfo();
}
