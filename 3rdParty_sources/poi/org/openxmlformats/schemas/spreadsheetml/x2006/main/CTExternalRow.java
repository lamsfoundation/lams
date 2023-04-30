/*
 * XML Type:  CT_ExternalRow
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ExternalRow(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTExternalRow extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalRow> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctexternalrowa22etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cell" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell> getCellList();

    /**
     * Gets array of all "cell" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell[] getCellArray();

    /**
     * Gets ith "cell" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell getCellArray(int i);

    /**
     * Returns number of "cell" element
     */
    int sizeOfCellArray();

    /**
     * Sets array of all "cell" element
     */
    void setCellArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell[] cellArray);

    /**
     * Sets ith "cell" element
     */
    void setCellArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell cell);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cell" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell insertNewCell(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cell" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalCell addNewCell();

    /**
     * Removes the ith "cell" element
     */
    void removeCell(int i);

    /**
     * Gets the "r" attribute
     */
    long getR();

    /**
     * Gets (as xml) the "r" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetR();

    /**
     * Sets the "r" attribute
     */
    void setR(long r);

    /**
     * Sets (as xml) the "r" attribute
     */
    void xsetR(org.apache.xmlbeans.XmlUnsignedInt r);
}
