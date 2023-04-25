/*
 * An XML document type.
 * Localname: CompleteRevocationRefs
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CompleteRevocationRefsDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one CompleteRevocationRefs(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface CompleteRevocationRefsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CompleteRevocationRefsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "completerevocationrefse9dbdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "CompleteRevocationRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteRevocationRefsType getCompleteRevocationRefs();

    /**
     * Sets the "CompleteRevocationRefs" element
     */
    void setCompleteRevocationRefs(org.etsi.uri.x01903.v13.CompleteRevocationRefsType completeRevocationRefs);

    /**
     * Appends and returns a new empty "CompleteRevocationRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteRevocationRefsType addNewCompleteRevocationRefs();
}
