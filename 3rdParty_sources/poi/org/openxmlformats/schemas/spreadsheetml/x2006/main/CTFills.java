/*
 * XML Type:  CT_Fills
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Fills(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFills extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfills2c6ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "fill" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill> getFillList();

    /**
     * Gets array of all "fill" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill[] getFillArray();

    /**
     * Gets ith "fill" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill getFillArray(int i);

    /**
     * Returns number of "fill" element
     */
    int sizeOfFillArray();

    /**
     * Sets array of all "fill" element
     */
    void setFillArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill[] fillArray);

    /**
     * Sets ith "fill" element
     */
    void setFillArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill fill);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "fill" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill insertNewFill(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "fill" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill addNewFill();

    /**
     * Removes the ith "fill" element
     */
    void removeFill(int i);

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
