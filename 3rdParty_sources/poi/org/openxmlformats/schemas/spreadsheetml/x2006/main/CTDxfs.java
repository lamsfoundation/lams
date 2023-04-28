/*
 * XML Type:  CT_Dxfs
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Dxfs(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDxfs extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdxfsb26atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "dxf" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf> getDxfList();

    /**
     * Gets array of all "dxf" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf[] getDxfArray();

    /**
     * Gets ith "dxf" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf getDxfArray(int i);

    /**
     * Returns number of "dxf" element
     */
    int sizeOfDxfArray();

    /**
     * Sets array of all "dxf" element
     */
    void setDxfArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf[] dxfArray);

    /**
     * Sets ith "dxf" element
     */
    void setDxfArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf dxf);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "dxf" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf insertNewDxf(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "dxf" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf addNewDxf();

    /**
     * Removes the ith "dxf" element
     */
    void removeDxf(int i);

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
