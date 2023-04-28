/*
 * An XML document type.
 * Localname: RetrievalMethod
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.RetrievalMethodDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one RetrievalMethod(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface RetrievalMethodDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.RetrievalMethodDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "retrievalmethod2e0cdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "RetrievalMethod" element
     */
    org.w3.x2000.x09.xmldsig.RetrievalMethodType getRetrievalMethod();

    /**
     * Sets the "RetrievalMethod" element
     */
    void setRetrievalMethod(org.w3.x2000.x09.xmldsig.RetrievalMethodType retrievalMethod);

    /**
     * Appends and returns a new empty "RetrievalMethod" element
     */
    org.w3.x2000.x09.xmldsig.RetrievalMethodType addNewRetrievalMethod();
}
