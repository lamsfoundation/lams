/*
 * XML Type:  X509IssuerSerialType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.X509IssuerSerialType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML X509IssuerSerialType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface X509IssuerSerialType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.X509IssuerSerialType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "x509issuerserialtype7eb2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "X509IssuerName" element
     */
    java.lang.String getX509IssuerName();

    /**
     * Gets (as xml) the "X509IssuerName" element
     */
    org.apache.xmlbeans.XmlString xgetX509IssuerName();

    /**
     * Sets the "X509IssuerName" element
     */
    void setX509IssuerName(java.lang.String x509IssuerName);

    /**
     * Sets (as xml) the "X509IssuerName" element
     */
    void xsetX509IssuerName(org.apache.xmlbeans.XmlString x509IssuerName);

    /**
     * Gets the "X509SerialNumber" element
     */
    java.math.BigInteger getX509SerialNumber();

    /**
     * Gets (as xml) the "X509SerialNumber" element
     */
    org.apache.xmlbeans.XmlInteger xgetX509SerialNumber();

    /**
     * Sets the "X509SerialNumber" element
     */
    void setX509SerialNumber(java.math.BigInteger x509SerialNumber);

    /**
     * Sets (as xml) the "X509SerialNumber" element
     */
    void xsetX509SerialNumber(org.apache.xmlbeans.XmlInteger x509SerialNumber);
}
