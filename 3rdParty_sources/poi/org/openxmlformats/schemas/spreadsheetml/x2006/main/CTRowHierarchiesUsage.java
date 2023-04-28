/*
 * XML Type:  CT_RowHierarchiesUsage
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowHierarchiesUsage
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RowHierarchiesUsage(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRowHierarchiesUsage extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowHierarchiesUsage> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrowhierarchiesusage59a7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "rowHierarchyUsage" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHierarchyUsage> getRowHierarchyUsageList();

    /**
     * Gets array of all "rowHierarchyUsage" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHierarchyUsage[] getRowHierarchyUsageArray();

    /**
     * Gets ith "rowHierarchyUsage" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHierarchyUsage getRowHierarchyUsageArray(int i);

    /**
     * Returns number of "rowHierarchyUsage" element
     */
    int sizeOfRowHierarchyUsageArray();

    /**
     * Sets array of all "rowHierarchyUsage" element
     */
    void setRowHierarchyUsageArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHierarchyUsage[] rowHierarchyUsageArray);

    /**
     * Sets ith "rowHierarchyUsage" element
     */
    void setRowHierarchyUsageArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHierarchyUsage rowHierarchyUsage);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rowHierarchyUsage" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHierarchyUsage insertNewRowHierarchyUsage(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "rowHierarchyUsage" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHierarchyUsage addNewRowHierarchyUsage();

    /**
     * Removes the ith "rowHierarchyUsage" element
     */
    void removeRowHierarchyUsage(int i);

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
