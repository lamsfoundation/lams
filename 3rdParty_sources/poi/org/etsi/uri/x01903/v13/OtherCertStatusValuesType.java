/*
 * XML Type:  OtherCertStatusValuesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OtherCertStatusValuesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML OtherCertStatusValuesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface OtherCertStatusValuesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.OtherCertStatusValuesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "othercertstatusvaluestypebf44type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "OtherValue" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.AnyType> getOtherValueList();

    /**
     * Gets array of all "OtherValue" elements
     */
    org.etsi.uri.x01903.v13.AnyType[] getOtherValueArray();

    /**
     * Gets ith "OtherValue" element
     */
    org.etsi.uri.x01903.v13.AnyType getOtherValueArray(int i);

    /**
     * Returns number of "OtherValue" element
     */
    int sizeOfOtherValueArray();

    /**
     * Sets array of all "OtherValue" element
     */
    void setOtherValueArray(org.etsi.uri.x01903.v13.AnyType[] otherValueArray);

    /**
     * Sets ith "OtherValue" element
     */
    void setOtherValueArray(int i, org.etsi.uri.x01903.v13.AnyType otherValue);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "OtherValue" element
     */
    org.etsi.uri.x01903.v13.AnyType insertNewOtherValue(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "OtherValue" element
     */
    org.etsi.uri.x01903.v13.AnyType addNewOtherValue();

    /**
     * Removes the ith "OtherValue" element
     */
    void removeOtherValue(int i);
}
