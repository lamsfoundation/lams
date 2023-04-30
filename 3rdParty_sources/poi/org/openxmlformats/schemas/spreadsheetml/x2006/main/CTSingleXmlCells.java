/*
 * XML Type:  CT_SingleXmlCells
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SingleXmlCells(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSingleXmlCells extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsinglexmlcells5a6btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "singleXmlCell" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell> getSingleXmlCellList();

    /**
     * Gets array of all "singleXmlCell" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell[] getSingleXmlCellArray();

    /**
     * Gets ith "singleXmlCell" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell getSingleXmlCellArray(int i);

    /**
     * Returns number of "singleXmlCell" element
     */
    int sizeOfSingleXmlCellArray();

    /**
     * Sets array of all "singleXmlCell" element
     */
    void setSingleXmlCellArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell[] singleXmlCellArray);

    /**
     * Sets ith "singleXmlCell" element
     */
    void setSingleXmlCellArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell singleXmlCell);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "singleXmlCell" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell insertNewSingleXmlCell(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "singleXmlCell" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell addNewSingleXmlCell();

    /**
     * Removes the ith "singleXmlCell" element
     */
    void removeSingleXmlCell(int i);
}
