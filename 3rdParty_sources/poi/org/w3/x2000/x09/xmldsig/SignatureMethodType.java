/*
 * XML Type:  SignatureMethodType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignatureMethodType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SignatureMethodType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface SignatureMethodType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.SignatureMethodType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signaturemethodtypefd5etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "HMACOutputLength" element
     */
    java.math.BigInteger getHMACOutputLength();

    /**
     * Gets (as xml) the "HMACOutputLength" element
     */
    org.w3.x2000.x09.xmldsig.HMACOutputLengthType xgetHMACOutputLength();

    /**
     * True if has "HMACOutputLength" element
     */
    boolean isSetHMACOutputLength();

    /**
     * Sets the "HMACOutputLength" element
     */
    void setHMACOutputLength(java.math.BigInteger hmacOutputLength);

    /**
     * Sets (as xml) the "HMACOutputLength" element
     */
    void xsetHMACOutputLength(org.w3.x2000.x09.xmldsig.HMACOutputLengthType hmacOutputLength);

    /**
     * Unsets the "HMACOutputLength" element
     */
    void unsetHMACOutputLength();

    /**
     * Gets the "Algorithm" attribute
     */
    java.lang.String getAlgorithm();

    /**
     * Gets (as xml) the "Algorithm" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetAlgorithm();

    /**
     * Sets the "Algorithm" attribute
     */
    void setAlgorithm(java.lang.String algorithm);

    /**
     * Sets (as xml) the "Algorithm" attribute
     */
    void xsetAlgorithm(org.apache.xmlbeans.XmlAnyURI algorithm);
}
