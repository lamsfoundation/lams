/*
 * XML Type:  IntegerListType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.IntegerListType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML IntegerListType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface IntegerListType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.IntegerListType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "integerlisttype20d8type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "int" elements
     */
    java.util.List<java.math.BigInteger> getIntList();

    /**
     * Gets array of all "int" elements
     */
    java.math.BigInteger[] getIntArray();

    /**
     * Gets ith "int" element
     */
    java.math.BigInteger getIntArray(int i);

    /**
     * Gets (as xml) a List of "int" elements
     */
    java.util.List<org.apache.xmlbeans.XmlInteger> xgetIntList();

    /**
     * Gets (as xml) array of all "int" elements
     */
    org.apache.xmlbeans.XmlInteger[] xgetIntArray();

    /**
     * Gets (as xml) ith "int" element
     */
    org.apache.xmlbeans.XmlInteger xgetIntArray(int i);

    /**
     * Returns number of "int" element
     */
    int sizeOfIntArray();

    /**
     * Sets array of all "int" element
     */
    void setIntArray(java.math.BigInteger[] xintArray);

    /**
     * Sets ith "int" element
     */
    void setIntArray(int i, java.math.BigInteger xint);

    /**
     * Sets (as xml) array of all "int" element
     */
    void xsetIntArray(org.apache.xmlbeans.XmlInteger[] xintArray);

    /**
     * Sets (as xml) ith "int" element
     */
    void xsetIntArray(int i, org.apache.xmlbeans.XmlInteger xint);

    /**
     * Inserts the value as the ith "int" element
     */
    void insertInt(int i, java.math.BigInteger xint);

    /**
     * Appends the value as the last "int" element
     */
    void addInt(java.math.BigInteger xint);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "int" element
     */
    org.apache.xmlbeans.XmlInteger insertNewInt(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "int" element
     */
    org.apache.xmlbeans.XmlInteger addNewInt();

    /**
     * Removes the ith "int" element
     */
    void removeInt(int i);
}
