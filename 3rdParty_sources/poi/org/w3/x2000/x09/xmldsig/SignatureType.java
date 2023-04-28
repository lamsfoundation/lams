/*
 * XML Type:  SignatureType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignatureType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SignatureType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface SignatureType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.SignatureType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signaturetype0a3ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignedInfo" element
     */
    org.w3.x2000.x09.xmldsig.SignedInfoType getSignedInfo();

    /**
     * Sets the "SignedInfo" element
     */
    void setSignedInfo(org.w3.x2000.x09.xmldsig.SignedInfoType signedInfo);

    /**
     * Appends and returns a new empty "SignedInfo" element
     */
    org.w3.x2000.x09.xmldsig.SignedInfoType addNewSignedInfo();

    /**
     * Gets the "SignatureValue" element
     */
    org.w3.x2000.x09.xmldsig.SignatureValueType getSignatureValue();

    /**
     * Sets the "SignatureValue" element
     */
    void setSignatureValue(org.w3.x2000.x09.xmldsig.SignatureValueType signatureValue);

    /**
     * Appends and returns a new empty "SignatureValue" element
     */
    org.w3.x2000.x09.xmldsig.SignatureValueType addNewSignatureValue();

    /**
     * Gets the "KeyInfo" element
     */
    org.w3.x2000.x09.xmldsig.KeyInfoType getKeyInfo();

    /**
     * True if has "KeyInfo" element
     */
    boolean isSetKeyInfo();

    /**
     * Sets the "KeyInfo" element
     */
    void setKeyInfo(org.w3.x2000.x09.xmldsig.KeyInfoType keyInfo);

    /**
     * Appends and returns a new empty "KeyInfo" element
     */
    org.w3.x2000.x09.xmldsig.KeyInfoType addNewKeyInfo();

    /**
     * Unsets the "KeyInfo" element
     */
    void unsetKeyInfo();

    /**
     * Gets a List of "Object" elements
     */
    java.util.List<org.w3.x2000.x09.xmldsig.ObjectType> getObjectList();

    /**
     * Gets array of all "Object" elements
     */
    org.w3.x2000.x09.xmldsig.ObjectType[] getObjectArray();

    /**
     * Gets ith "Object" element
     */
    org.w3.x2000.x09.xmldsig.ObjectType getObjectArray(int i);

    /**
     * Returns number of "Object" element
     */
    int sizeOfObjectArray();

    /**
     * Sets array of all "Object" element
     */
    void setObjectArray(org.w3.x2000.x09.xmldsig.ObjectType[] objectArray);

    /**
     * Sets ith "Object" element
     */
    void setObjectArray(int i, org.w3.x2000.x09.xmldsig.ObjectType object);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Object" element
     */
    org.w3.x2000.x09.xmldsig.ObjectType insertNewObject(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "Object" element
     */
    org.w3.x2000.x09.xmldsig.ObjectType addNewObject();

    /**
     * Removes the ith "Object" element
     */
    void removeObject(int i);

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
