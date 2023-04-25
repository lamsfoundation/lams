/*
 * XML Type:  CT_QueryTableDeletedFields
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryTableDeletedFields
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_QueryTableDeletedFields(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTQueryTableDeletedFields extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryTableDeletedFields> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctquerytabledeletedfieldse74btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "deletedField" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDeletedField> getDeletedFieldList();

    /**
     * Gets array of all "deletedField" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDeletedField[] getDeletedFieldArray();

    /**
     * Gets ith "deletedField" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDeletedField getDeletedFieldArray(int i);

    /**
     * Returns number of "deletedField" element
     */
    int sizeOfDeletedFieldArray();

    /**
     * Sets array of all "deletedField" element
     */
    void setDeletedFieldArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDeletedField[] deletedFieldArray);

    /**
     * Sets ith "deletedField" element
     */
    void setDeletedFieldArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDeletedField deletedField);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "deletedField" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDeletedField insertNewDeletedField(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "deletedField" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDeletedField addNewDeletedField();

    /**
     * Removes the ith "deletedField" element
     */
    void removeDeletedField(int i);

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
