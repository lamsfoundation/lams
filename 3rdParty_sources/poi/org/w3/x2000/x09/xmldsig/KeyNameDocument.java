/*
 * An XML document type.
 * Localname: KeyName
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.KeyNameDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one KeyName(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface KeyNameDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.KeyNameDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "keyname5697doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "KeyName" element
     */
    java.lang.String getKeyName();

    /**
     * Gets (as xml) the "KeyName" element
     */
    org.apache.xmlbeans.XmlString xgetKeyName();

    /**
     * Sets the "KeyName" element
     */
    void setKeyName(java.lang.String keyName);

    /**
     * Sets (as xml) the "KeyName" element
     */
    void xsetKeyName(org.apache.xmlbeans.XmlString keyName);
}
