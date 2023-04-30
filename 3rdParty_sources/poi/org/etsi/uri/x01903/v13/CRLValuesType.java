/*
 * XML Type:  CRLValuesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CRLValuesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CRLValuesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CRLValuesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CRLValuesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "crlvaluestype0ebbtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "EncapsulatedCRLValue" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.EncapsulatedPKIDataType> getEncapsulatedCRLValueList();

    /**
     * Gets array of all "EncapsulatedCRLValue" elements
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] getEncapsulatedCRLValueArray();

    /**
     * Gets ith "EncapsulatedCRLValue" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType getEncapsulatedCRLValueArray(int i);

    /**
     * Returns number of "EncapsulatedCRLValue" element
     */
    int sizeOfEncapsulatedCRLValueArray();

    /**
     * Sets array of all "EncapsulatedCRLValue" element
     */
    void setEncapsulatedCRLValueArray(org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] encapsulatedCRLValueArray);

    /**
     * Sets ith "EncapsulatedCRLValue" element
     */
    void setEncapsulatedCRLValueArray(int i, org.etsi.uri.x01903.v13.EncapsulatedPKIDataType encapsulatedCRLValue);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "EncapsulatedCRLValue" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType insertNewEncapsulatedCRLValue(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "EncapsulatedCRLValue" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType addNewEncapsulatedCRLValue();

    /**
     * Removes the ith "EncapsulatedCRLValue" element
     */
    void removeEncapsulatedCRLValue(int i);
}
