/*
 * XML Type:  CT_SdtComboBox
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtComboBox
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SdtComboBox(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSdtComboBox extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtComboBox> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsdtcomboboxdb52type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "listItem" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtListItem> getListItemList();

    /**
     * Gets array of all "listItem" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtListItem[] getListItemArray();

    /**
     * Gets ith "listItem" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtListItem getListItemArray(int i);

    /**
     * Returns number of "listItem" element
     */
    int sizeOfListItemArray();

    /**
     * Sets array of all "listItem" element
     */
    void setListItemArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtListItem[] listItemArray);

    /**
     * Sets ith "listItem" element
     */
    void setListItemArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtListItem listItem);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "listItem" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtListItem insertNewListItem(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "listItem" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtListItem addNewListItem();

    /**
     * Removes the ith "listItem" element
     */
    void removeListItem(int i);

    /**
     * Gets the "lastValue" attribute
     */
    java.lang.String getLastValue();

    /**
     * Gets (as xml) the "lastValue" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetLastValue();

    /**
     * True if has "lastValue" attribute
     */
    boolean isSetLastValue();

    /**
     * Sets the "lastValue" attribute
     */
    void setLastValue(java.lang.String lastValue);

    /**
     * Sets (as xml) the "lastValue" attribute
     */
    void xsetLastValue(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString lastValue);

    /**
     * Unsets the "lastValue" attribute
     */
    void unsetLastValue();
}
