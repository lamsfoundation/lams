/*
 * An XML document type.
 * Localname: Include
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.IncludeDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one Include(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface IncludeDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.IncludeDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "include7f9adoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Include" element
     */
    org.etsi.uri.x01903.v13.IncludeType getInclude();

    /**
     * Sets the "Include" element
     */
    void setInclude(org.etsi.uri.x01903.v13.IncludeType include);

    /**
     * Appends and returns a new empty "Include" element
     */
    org.etsi.uri.x01903.v13.IncludeType addNewInclude();
}
