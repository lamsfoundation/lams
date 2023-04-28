/*
 * XML Type:  CT_PageFields
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PageFields(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPageFields extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpagefields1db1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "pageField" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageField> getPageFieldList();

    /**
     * Gets array of all "pageField" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageField[] getPageFieldArray();

    /**
     * Gets ith "pageField" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageField getPageFieldArray(int i);

    /**
     * Returns number of "pageField" element
     */
    int sizeOfPageFieldArray();

    /**
     * Sets array of all "pageField" element
     */
    void setPageFieldArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageField[] pageFieldArray);

    /**
     * Sets ith "pageField" element
     */
    void setPageFieldArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageField pageField);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pageField" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageField insertNewPageField(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "pageField" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageField addNewPageField();

    /**
     * Removes the ith "pageField" element
     */
    void removePageField(int i);

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
