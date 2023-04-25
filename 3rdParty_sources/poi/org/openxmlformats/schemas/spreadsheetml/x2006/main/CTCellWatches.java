/*
 * XML Type:  CT_CellWatches
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatches
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CellWatches(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCellWatches extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatches> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcellwatches531atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cellWatch" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch> getCellWatchList();

    /**
     * Gets array of all "cellWatch" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch[] getCellWatchArray();

    /**
     * Gets ith "cellWatch" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch getCellWatchArray(int i);

    /**
     * Returns number of "cellWatch" element
     */
    int sizeOfCellWatchArray();

    /**
     * Sets array of all "cellWatch" element
     */
    void setCellWatchArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch[] cellWatchArray);

    /**
     * Sets ith "cellWatch" element
     */
    void setCellWatchArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch cellWatch);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cellWatch" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch insertNewCellWatch(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cellWatch" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatch addNewCellWatch();

    /**
     * Removes the ith "cellWatch" element
     */
    void removeCellWatch(int i);
}
