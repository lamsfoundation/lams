/*
 * An XML document type.
 * Localname: QualifyingPropertiesReference
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one QualifyingPropertiesReference(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface QualifyingPropertiesReferenceDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "qualifyingpropertiesreferencee095doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "QualifyingPropertiesReference" element
     */
    org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceType getQualifyingPropertiesReference();

    /**
     * Sets the "QualifyingPropertiesReference" element
     */
    void setQualifyingPropertiesReference(org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceType qualifyingPropertiesReference);

    /**
     * Appends and returns a new empty "QualifyingPropertiesReference" element
     */
    org.etsi.uri.x01903.v13.QualifyingPropertiesReferenceType addNewQualifyingPropertiesReference();
}
