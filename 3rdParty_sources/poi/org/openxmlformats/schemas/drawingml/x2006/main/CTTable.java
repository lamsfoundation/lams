/*
 * XML Type:  CT_Table
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTable
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Table(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTable extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTable> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttable5f3ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tblPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties getTblPr();

    /**
     * True if has "tblPr" element
     */
    boolean isSetTblPr();

    /**
     * Sets the "tblPr" element
     */
    void setTblPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties tblPr);

    /**
     * Appends and returns a new empty "tblPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties addNewTblPr();

    /**
     * Unsets the "tblPr" element
     */
    void unsetTblPr();

    /**
     * Gets the "tblGrid" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid getTblGrid();

    /**
     * Sets the "tblGrid" element
     */
    void setTblGrid(org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid tblGrid);

    /**
     * Appends and returns a new empty "tblGrid" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid addNewTblGrid();

    /**
     * Gets a List of "tr" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow> getTrList();

    /**
     * Gets array of all "tr" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow[] getTrArray();

    /**
     * Gets ith "tr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow getTrArray(int i);

    /**
     * Returns number of "tr" element
     */
    int sizeOfTrArray();

    /**
     * Sets array of all "tr" element
     */
    void setTrArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow[] trArray);

    /**
     * Sets ith "tr" element
     */
    void setTrArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow tr);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow insertNewTr(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "tr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow addNewTr();

    /**
     * Removes the ith "tr" element
     */
    void removeTr(int i);
}
