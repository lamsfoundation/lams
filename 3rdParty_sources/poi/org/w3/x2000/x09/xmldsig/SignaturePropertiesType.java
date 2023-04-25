/*
 * XML Type:  SignaturePropertiesType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignaturePropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SignaturePropertiesType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface SignaturePropertiesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.SignaturePropertiesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signaturepropertiestype884ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "SignatureProperty" elements
     */
    java.util.List<org.w3.x2000.x09.xmldsig.SignaturePropertyType> getSignaturePropertyList();

    /**
     * Gets array of all "SignatureProperty" elements
     */
    org.w3.x2000.x09.xmldsig.SignaturePropertyType[] getSignaturePropertyArray();

    /**
     * Gets ith "SignatureProperty" element
     */
    org.w3.x2000.x09.xmldsig.SignaturePropertyType getSignaturePropertyArray(int i);

    /**
     * Returns number of "SignatureProperty" element
     */
    int sizeOfSignaturePropertyArray();

    /**
     * Sets array of all "SignatureProperty" element
     */
    void setSignaturePropertyArray(org.w3.x2000.x09.xmldsig.SignaturePropertyType[] signaturePropertyArray);

    /**
     * Sets ith "SignatureProperty" element
     */
    void setSignaturePropertyArray(int i, org.w3.x2000.x09.xmldsig.SignaturePropertyType signatureProperty);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "SignatureProperty" element
     */
    org.w3.x2000.x09.xmldsig.SignaturePropertyType insertNewSignatureProperty(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "SignatureProperty" element
     */
    org.w3.x2000.x09.xmldsig.SignaturePropertyType addNewSignatureProperty();

    /**
     * Removes the ith "SignatureProperty" element
     */
    void removeSignatureProperty(int i);

    /**
     * Gets the "Id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "Id" attribute
     */
    org.apache.xmlbeans.XmlID xgetId();

    /**
     * True if has "Id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "Id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "Id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlID id);

    /**
     * Unsets the "Id" attribute
     */
    void unsetId();
}
