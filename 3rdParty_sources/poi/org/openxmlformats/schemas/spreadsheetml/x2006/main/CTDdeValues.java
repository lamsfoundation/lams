/*
 * XML Type:  CT_DdeValues
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValues
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DdeValues(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDdeValues extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValues> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctddevalues8e46type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "value" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue> getValueList();

    /**
     * Gets array of all "value" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue[] getValueArray();

    /**
     * Gets ith "value" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue getValueArray(int i);

    /**
     * Returns number of "value" element
     */
    int sizeOfValueArray();

    /**
     * Sets array of all "value" element
     */
    void setValueArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue[] valueArray);

    /**
     * Sets ith "value" element
     */
    void setValueArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue value);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "value" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue insertNewValue(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "value" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue addNewValue();

    /**
     * Removes the ith "value" element
     */
    void removeValue(int i);

    /**
     * Gets the "rows" attribute
     */
    long getRows();

    /**
     * Gets (as xml) the "rows" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetRows();

    /**
     * True if has "rows" attribute
     */
    boolean isSetRows();

    /**
     * Sets the "rows" attribute
     */
    void setRows(long rows);

    /**
     * Sets (as xml) the "rows" attribute
     */
    void xsetRows(org.apache.xmlbeans.XmlUnsignedInt rows);

    /**
     * Unsets the "rows" attribute
     */
    void unsetRows();

    /**
     * Gets the "cols" attribute
     */
    long getCols();

    /**
     * Gets (as xml) the "cols" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCols();

    /**
     * True if has "cols" attribute
     */
    boolean isSetCols();

    /**
     * Sets the "cols" attribute
     */
    void setCols(long cols);

    /**
     * Sets (as xml) the "cols" attribute
     */
    void xsetCols(org.apache.xmlbeans.XmlUnsignedInt cols);

    /**
     * Unsets the "cols" attribute
     */
    void unsetCols();
}
