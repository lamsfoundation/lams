/*
 * XML Type:  CT_AdditionalCharacteristics
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/characteristics
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.characteristics;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AdditionalCharacteristics(@http://schemas.openxmlformats.org/officeDocument/2006/characteristics).
 *
 * This is a complex type.
 */
public interface CTAdditionalCharacteristics extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctadditionalcharacteristics01f5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "characteristic" elements
     */
    java.util.List<org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic> getCharacteristicList();

    /**
     * Gets array of all "characteristic" elements
     */
    org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic[] getCharacteristicArray();

    /**
     * Gets ith "characteristic" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic getCharacteristicArray(int i);

    /**
     * Returns number of "characteristic" element
     */
    int sizeOfCharacteristicArray();

    /**
     * Sets array of all "characteristic" element
     */
    void setCharacteristicArray(org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic[] characteristicArray);

    /**
     * Sets ith "characteristic" element
     */
    void setCharacteristicArray(int i, org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic characteristic);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "characteristic" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic insertNewCharacteristic(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "characteristic" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic addNewCharacteristic();

    /**
     * Removes the ith "characteristic" element
     */
    void removeCharacteristic(int i);
}
