/*
 * XML Type:  CT_TextFields
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTextFields
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextFields(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTextFields extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTextFields> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttextfields104ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "textField" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTextField> getTextFieldList();

    /**
     * Gets array of all "textField" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTextField[] getTextFieldArray();

    /**
     * Gets ith "textField" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTextField getTextFieldArray(int i);

    /**
     * Returns number of "textField" element
     */
    int sizeOfTextFieldArray();

    /**
     * Sets array of all "textField" element
     */
    void setTextFieldArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTextField[] textFieldArray);

    /**
     * Sets ith "textField" element
     */
    void setTextFieldArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTextField textField);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "textField" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTextField insertNewTextField(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "textField" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTextField addNewTextField();

    /**
     * Removes the ith "textField" element
     */
    void removeTextField(int i);

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
