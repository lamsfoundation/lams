/*
 * XML Type:  CT_CellStyleXfs
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CellStyleXfs(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCellStyleXfs extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcellstylexfsa81ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "xf" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf> getXfList();

    /**
     * Gets array of all "xf" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf[] getXfArray();

    /**
     * Gets ith "xf" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf getXfArray(int i);

    /**
     * Returns number of "xf" element
     */
    int sizeOfXfArray();

    /**
     * Sets array of all "xf" element
     */
    void setXfArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf[] xfArray);

    /**
     * Sets ith "xf" element
     */
    void setXfArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf xf);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "xf" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf insertNewXf(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "xf" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf addNewXf();

    /**
     * Removes the ith "xf" element
     */
    void removeXf(int i);

    /**
     * Gets the "count" attribute
     */
    long getCount();

    /**
     * Gets (as xml) the "count" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCount();

    /**
     * True if has "count" attribute
     */
    boolean isSetCount();

    /**
     * Sets the "count" attribute
     */
    void setCount(long count);

    /**
     * Sets (as xml) the "count" attribute
     */
    void xsetCount(org.apache.xmlbeans.XmlUnsignedInt count);

    /**
     * Unsets the "count" attribute
     */
    void unsetCount();
}
