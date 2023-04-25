/*
 * An XML document type.
 * Localname: DigestMethod
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.DigestMethodDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one DigestMethod(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface DigestMethodDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.DigestMethodDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "digestmethod576adoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "DigestMethod" element
     */
    org.w3.x2000.x09.xmldsig.DigestMethodType getDigestMethod();

    /**
     * Sets the "DigestMethod" element
     */
    void setDigestMethod(org.w3.x2000.x09.xmldsig.DigestMethodType digestMethod);

    /**
     * Appends and returns a new empty "DigestMethod" element
     */
    org.w3.x2000.x09.xmldsig.DigestMethodType addNewDigestMethod();
}
