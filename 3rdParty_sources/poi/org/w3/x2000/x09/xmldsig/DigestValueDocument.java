/*
 * An XML document type.
 * Localname: DigestValue
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.DigestValueDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one DigestValue(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface DigestValueDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.DigestValueDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "digestvalue22b4doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "DigestValue" element
     */
    byte[] getDigestValue();

    /**
     * Gets (as xml) the "DigestValue" element
     */
    org.w3.x2000.x09.xmldsig.DigestValueType xgetDigestValue();

    /**
     * Sets the "DigestValue" element
     */
    void setDigestValue(byte[] digestValue);

    /**
     * Sets (as xml) the "DigestValue" element
     */
    void xsetDigestValue(org.w3.x2000.x09.xmldsig.DigestValueType digestValue);
}
