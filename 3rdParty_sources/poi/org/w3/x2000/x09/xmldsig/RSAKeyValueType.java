/*
 * XML Type:  RSAKeyValueType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.RSAKeyValueType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML RSAKeyValueType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface RSAKeyValueType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.RSAKeyValueType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "rsakeyvaluetypefec5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Modulus" element
     */
    byte[] getModulus();

    /**
     * Gets (as xml) the "Modulus" element
     */
    org.w3.x2000.x09.xmldsig.CryptoBinary xgetModulus();

    /**
     * Sets the "Modulus" element
     */
    void setModulus(byte[] modulus);

    /**
     * Sets (as xml) the "Modulus" element
     */
    void xsetModulus(org.w3.x2000.x09.xmldsig.CryptoBinary modulus);

    /**
     * Gets the "Exponent" element
     */
    byte[] getExponent();

    /**
     * Gets (as xml) the "Exponent" element
     */
    org.w3.x2000.x09.xmldsig.CryptoBinary xgetExponent();

    /**
     * Sets the "Exponent" element
     */
    void setExponent(byte[] exponent);

    /**
     * Sets (as xml) the "Exponent" element
     */
    void xsetExponent(org.w3.x2000.x09.xmldsig.CryptoBinary exponent);
}
