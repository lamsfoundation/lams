/*
 * XML Type:  CT_CustomProperties
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CustomProperties(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCustomProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcustomproperties584dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "customPr" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty> getCustomPrList();

    /**
     * Gets array of all "customPr" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty[] getCustomPrArray();

    /**
     * Gets ith "customPr" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty getCustomPrArray(int i);

    /**
     * Returns number of "customPr" element
     */
    int sizeOfCustomPrArray();

    /**
     * Sets array of all "customPr" element
     */
    void setCustomPrArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty[] customPrArray);

    /**
     * Sets ith "customPr" element
     */
    void setCustomPrArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty customPr);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customPr" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty insertNewCustomPr(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "customPr" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty addNewCustomPr();

    /**
     * Removes the ith "customPr" element
     */
    void removeCustomPr(int i);
}
