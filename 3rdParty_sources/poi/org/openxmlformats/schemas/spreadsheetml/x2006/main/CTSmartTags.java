/*
 * XML Type:  CT_SmartTags
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTags
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SmartTags(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSmartTags extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTags> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsmarttags8aa1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cellSmartTags" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags> getCellSmartTagsList();

    /**
     * Gets array of all "cellSmartTags" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags[] getCellSmartTagsArray();

    /**
     * Gets ith "cellSmartTags" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags getCellSmartTagsArray(int i);

    /**
     * Returns number of "cellSmartTags" element
     */
    int sizeOfCellSmartTagsArray();

    /**
     * Sets array of all "cellSmartTags" element
     */
    void setCellSmartTagsArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags[] cellSmartTagsArray);

    /**
     * Sets ith "cellSmartTags" element
     */
    void setCellSmartTagsArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags cellSmartTags);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cellSmartTags" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags insertNewCellSmartTags(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cellSmartTags" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags addNewCellSmartTags();

    /**
     * Removes the ith "cellSmartTags" element
     */
    void removeCellSmartTags(int i);
}
