/*
 * XML Type:  CT_CellSmartTags
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CellSmartTags(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCellSmartTags extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcellsmarttagsb05ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cellSmartTag" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag> getCellSmartTagList();

    /**
     * Gets array of all "cellSmartTag" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag[] getCellSmartTagArray();

    /**
     * Gets ith "cellSmartTag" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag getCellSmartTagArray(int i);

    /**
     * Returns number of "cellSmartTag" element
     */
    int sizeOfCellSmartTagArray();

    /**
     * Sets array of all "cellSmartTag" element
     */
    void setCellSmartTagArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag[] cellSmartTagArray);

    /**
     * Sets ith "cellSmartTag" element
     */
    void setCellSmartTagArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag cellSmartTag);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cellSmartTag" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag insertNewCellSmartTag(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cellSmartTag" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag addNewCellSmartTag();

    /**
     * Removes the ith "cellSmartTag" element
     */
    void removeCellSmartTag(int i);

    /**
     * Gets the "r" attribute
     */
    java.lang.String getR();

    /**
     * Gets (as xml) the "r" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef xgetR();

    /**
     * Sets the "r" attribute
     */
    void setR(java.lang.String r);

    /**
     * Sets (as xml) the "r" attribute
     */
    void xsetR(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef r);
}
