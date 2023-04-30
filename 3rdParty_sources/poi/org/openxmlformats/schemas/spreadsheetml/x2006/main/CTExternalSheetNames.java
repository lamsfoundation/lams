/*
 * XML Type:  CT_ExternalSheetNames
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetNames
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ExternalSheetNames(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTExternalSheetNames extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetNames> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctexternalsheetnames7eddtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "sheetName" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName> getSheetNameList();

    /**
     * Gets array of all "sheetName" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName[] getSheetNameArray();

    /**
     * Gets ith "sheetName" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName getSheetNameArray(int i);

    /**
     * Returns number of "sheetName" element
     */
    int sizeOfSheetNameArray();

    /**
     * Sets array of all "sheetName" element
     */
    void setSheetNameArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName[] sheetNameArray);

    /**
     * Sets ith "sheetName" element
     */
    void setSheetNameArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName sheetName);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sheetName" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName insertNewSheetName(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "sheetName" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName addNewSheetName();

    /**
     * Removes the ith "sheetName" element
     */
    void removeSheetName(int i);
}
