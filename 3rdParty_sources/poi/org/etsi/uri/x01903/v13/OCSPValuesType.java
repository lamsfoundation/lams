/*
 * XML Type:  OCSPValuesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OCSPValuesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML OCSPValuesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface OCSPValuesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.OCSPValuesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ocspvaluestypeb421type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "EncapsulatedOCSPValue" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.EncapsulatedPKIDataType> getEncapsulatedOCSPValueList();

    /**
     * Gets array of all "EncapsulatedOCSPValue" elements
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] getEncapsulatedOCSPValueArray();

    /**
     * Gets ith "EncapsulatedOCSPValue" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType getEncapsulatedOCSPValueArray(int i);

    /**
     * Returns number of "EncapsulatedOCSPValue" element
     */
    int sizeOfEncapsulatedOCSPValueArray();

    /**
     * Sets array of all "EncapsulatedOCSPValue" element
     */
    void setEncapsulatedOCSPValueArray(org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] encapsulatedOCSPValueArray);

    /**
     * Sets ith "EncapsulatedOCSPValue" element
     */
    void setEncapsulatedOCSPValueArray(int i, org.etsi.uri.x01903.v13.EncapsulatedPKIDataType encapsulatedOCSPValue);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "EncapsulatedOCSPValue" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType insertNewEncapsulatedOCSPValue(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "EncapsulatedOCSPValue" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType addNewEncapsulatedOCSPValue();

    /**
     * Removes the ith "EncapsulatedOCSPValue" element
     */
    void removeEncapsulatedOCSPValue(int i);
}
