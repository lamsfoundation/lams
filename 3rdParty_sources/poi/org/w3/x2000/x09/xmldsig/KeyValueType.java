/*
 * XML Type:  KeyValueType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.KeyValueType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML KeyValueType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface KeyValueType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.KeyValueType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "keyvaluetype1d33type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "DSAKeyValue" element
     */
    org.w3.x2000.x09.xmldsig.DSAKeyValueType getDSAKeyValue();

    /**
     * True if has "DSAKeyValue" element
     */
    boolean isSetDSAKeyValue();

    /**
     * Sets the "DSAKeyValue" element
     */
    void setDSAKeyValue(org.w3.x2000.x09.xmldsig.DSAKeyValueType dsaKeyValue);

    /**
     * Appends and returns a new empty "DSAKeyValue" element
     */
    org.w3.x2000.x09.xmldsig.DSAKeyValueType addNewDSAKeyValue();

    /**
     * Unsets the "DSAKeyValue" element
     */
    void unsetDSAKeyValue();

    /**
     * Gets the "RSAKeyValue" element
     */
    org.w3.x2000.x09.xmldsig.RSAKeyValueType getRSAKeyValue();

    /**
     * True if has "RSAKeyValue" element
     */
    boolean isSetRSAKeyValue();

    /**
     * Sets the "RSAKeyValue" element
     */
    void setRSAKeyValue(org.w3.x2000.x09.xmldsig.RSAKeyValueType rsaKeyValue);

    /**
     * Appends and returns a new empty "RSAKeyValue" element
     */
    org.w3.x2000.x09.xmldsig.RSAKeyValueType addNewRSAKeyValue();

    /**
     * Unsets the "RSAKeyValue" element
     */
    void unsetRSAKeyValue();
}
