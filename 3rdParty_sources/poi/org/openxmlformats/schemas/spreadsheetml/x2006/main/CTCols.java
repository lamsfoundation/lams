/*
 * XML Type:  CT_Cols
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Cols(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCols extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcols627ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "col" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol> getColList();

    /**
     * Gets array of all "col" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol[] getColArray();

    /**
     * Gets ith "col" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol getColArray(int i);

    /**
     * Returns number of "col" element
     */
    int sizeOfColArray();

    /**
     * Sets array of all "col" element
     */
    void setColArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol[] colArray);

    /**
     * Sets ith "col" element
     */
    void setColArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol col);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "col" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol insertNewCol(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "col" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol addNewCol();

    /**
     * Removes the ith "col" element
     */
    void removeCol(int i);
}
