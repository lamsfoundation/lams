/*
 * An XML document type.
 * Localname: CanonicalizationMethod
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.CanonicalizationMethodDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one CanonicalizationMethod(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface CanonicalizationMethodDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.CanonicalizationMethodDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "canonicalizationmethoda4fedoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "CanonicalizationMethod" element
     */
    org.w3.x2000.x09.xmldsig.CanonicalizationMethodType getCanonicalizationMethod();

    /**
     * Sets the "CanonicalizationMethod" element
     */
    void setCanonicalizationMethod(org.w3.x2000.x09.xmldsig.CanonicalizationMethodType canonicalizationMethod);

    /**
     * Appends and returns a new empty "CanonicalizationMethod" element
     */
    org.w3.x2000.x09.xmldsig.CanonicalizationMethodType addNewCanonicalizationMethod();
}
