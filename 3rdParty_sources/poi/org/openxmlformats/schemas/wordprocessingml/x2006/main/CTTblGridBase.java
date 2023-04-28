/*
 * XML Type:  CT_TblGridBase
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TblGridBase(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTblGridBase extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttblgridbasea11dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "gridCol" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol> getGridColList();

    /**
     * Gets array of all "gridCol" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol[] getGridColArray();

    /**
     * Gets ith "gridCol" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol getGridColArray(int i);

    /**
     * Returns number of "gridCol" element
     */
    int sizeOfGridColArray();

    /**
     * Sets array of all "gridCol" element
     */
    void setGridColArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol[] gridColArray);

    /**
     * Sets ith "gridCol" element
     */
    void setGridColArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol gridCol);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gridCol" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol insertNewGridCol(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "gridCol" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol addNewGridCol();

    /**
     * Removes the ith "gridCol" element
     */
    void removeGridCol(int i);
}
