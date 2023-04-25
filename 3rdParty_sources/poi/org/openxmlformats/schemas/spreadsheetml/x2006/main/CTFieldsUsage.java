/*
 * XML Type:  CT_FieldsUsage
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldsUsage
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FieldsUsage(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFieldsUsage extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldsUsage> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfieldsusagee547type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "fieldUsage" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldUsage> getFieldUsageList();

    /**
     * Gets array of all "fieldUsage" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldUsage[] getFieldUsageArray();

    /**
     * Gets ith "fieldUsage" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldUsage getFieldUsageArray(int i);

    /**
     * Returns number of "fieldUsage" element
     */
    int sizeOfFieldUsageArray();

    /**
     * Sets array of all "fieldUsage" element
     */
    void setFieldUsageArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldUsage[] fieldUsageArray);

    /**
     * Sets ith "fieldUsage" element
     */
    void setFieldUsageArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldUsage fieldUsage);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "fieldUsage" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldUsage insertNewFieldUsage(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "fieldUsage" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldUsage addNewFieldUsage();

    /**
     * Removes the ith "fieldUsage" element
     */
    void removeFieldUsage(int i);

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
