/*
 * An XML document type.
 * Localname: QualifyingProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.QualifyingPropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one QualifyingProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface QualifyingPropertiesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.QualifyingPropertiesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "qualifyingproperties53ccdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "QualifyingProperties" element
     */
    org.etsi.uri.x01903.v13.QualifyingPropertiesType getQualifyingProperties();

    /**
     * Sets the "QualifyingProperties" element
     */
    void setQualifyingProperties(org.etsi.uri.x01903.v13.QualifyingPropertiesType qualifyingProperties);

    /**
     * Appends and returns a new empty "QualifyingProperties" element
     */
    org.etsi.uri.x01903.v13.QualifyingPropertiesType addNewQualifyingProperties();
}
