/*
 * XML Type:  SPKIDataType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SPKIDataType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SPKIDataType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface SPKIDataType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.SPKIDataType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "spkidatatypea180type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "SPKISexp" elements
     */
    java.util.List<byte[]> getSPKISexpList();

    /**
     * Gets array of all "SPKISexp" elements
     */
    byte[][] getSPKISexpArray();

    /**
     * Gets ith "SPKISexp" element
     */
    byte[] getSPKISexpArray(int i);

    /**
     * Gets (as xml) a List of "SPKISexp" elements
     */
    java.util.List<org.apache.xmlbeans.XmlBase64Binary> xgetSPKISexpList();

    /**
     * Gets (as xml) array of all "SPKISexp" elements
     */
    org.apache.xmlbeans.XmlBase64Binary[] xgetSPKISexpArray();

    /**
     * Gets (as xml) ith "SPKISexp" element
     */
    org.apache.xmlbeans.XmlBase64Binary xgetSPKISexpArray(int i);

    /**
     * Returns number of "SPKISexp" element
     */
    int sizeOfSPKISexpArray();

    /**
     * Sets array of all "SPKISexp" element
     */
    void setSPKISexpArray(byte[][] spkiSexpArray);

    /**
     * Sets ith "SPKISexp" element
     */
    void setSPKISexpArray(int i, byte[] spkiSexp);

    /**
     * Sets (as xml) array of all "SPKISexp" element
     */
    void xsetSPKISexpArray(org.apache.xmlbeans.XmlBase64Binary[] spkiSexpArray);

    /**
     * Sets (as xml) ith "SPKISexp" element
     */
    void xsetSPKISexpArray(int i, org.apache.xmlbeans.XmlBase64Binary spkiSexp);

    /**
     * Inserts the value as the ith "SPKISexp" element
     */
    void insertSPKISexp(int i, byte[] spkiSexp);

    /**
     * Appends the value as the last "SPKISexp" element
     */
    void addSPKISexp(byte[] spkiSexp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "SPKISexp" element
     */
    org.apache.xmlbeans.XmlBase64Binary insertNewSPKISexp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "SPKISexp" element
     */
    org.apache.xmlbeans.XmlBase64Binary addNewSPKISexp();

    /**
     * Removes the ith "SPKISexp" element
     */
    void removeSPKISexp(int i);
}
