/*
 * XML Type:  CT_CalculatedItems
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItems
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CalculatedItems(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCalculatedItems extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItems> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcalculateditemsed81type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "calculatedItem" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItem> getCalculatedItemList();

    /**
     * Gets array of all "calculatedItem" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItem[] getCalculatedItemArray();

    /**
     * Gets ith "calculatedItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItem getCalculatedItemArray(int i);

    /**
     * Returns number of "calculatedItem" element
     */
    int sizeOfCalculatedItemArray();

    /**
     * Sets array of all "calculatedItem" element
     */
    void setCalculatedItemArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItem[] calculatedItemArray);

    /**
     * Sets ith "calculatedItem" element
     */
    void setCalculatedItemArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItem calculatedItem);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "calculatedItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItem insertNewCalculatedItem(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "calculatedItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItem addNewCalculatedItem();

    /**
     * Removes the ith "calculatedItem" element
     */
    void removeCalculatedItem(int i);

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
