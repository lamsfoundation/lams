/*
 * XML Type:  CT_TableGrid
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TableGrid(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTableGrid extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttablegrid69a5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "gridCol" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol> getGridColList();

    /**
     * Gets array of all "gridCol" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol[] getGridColArray();

    /**
     * Gets ith "gridCol" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol getGridColArray(int i);

    /**
     * Returns number of "gridCol" element
     */
    int sizeOfGridColArray();

    /**
     * Sets array of all "gridCol" element
     */
    void setGridColArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol[] gridColArray);

    /**
     * Sets ith "gridCol" element
     */
    void setGridColArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol gridCol);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gridCol" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol insertNewGridCol(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "gridCol" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol addNewGridCol();

    /**
     * Removes the ith "gridCol" element
     */
    void removeGridCol(int i);
}
