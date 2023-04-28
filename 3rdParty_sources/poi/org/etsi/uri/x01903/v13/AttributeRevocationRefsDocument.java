/*
 * An XML document type.
 * Localname: AttributeRevocationRefs
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.AttributeRevocationRefsDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one AttributeRevocationRefs(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface AttributeRevocationRefsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.AttributeRevocationRefsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "attributerevocationrefsc92cdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "AttributeRevocationRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteRevocationRefsType getAttributeRevocationRefs();

    /**
     * Sets the "AttributeRevocationRefs" element
     */
    void setAttributeRevocationRefs(org.etsi.uri.x01903.v13.CompleteRevocationRefsType attributeRevocationRefs);

    /**
     * Appends and returns a new empty "AttributeRevocationRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteRevocationRefsType addNewAttributeRevocationRefs();
}
