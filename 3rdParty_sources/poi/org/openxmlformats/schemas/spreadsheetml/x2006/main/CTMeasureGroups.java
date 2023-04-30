/*
 * XML Type:  CT_MeasureGroups
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroups
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MeasureGroups(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMeasureGroups extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroups> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmeasuregroups2c71type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "measureGroup" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroup> getMeasureGroupList();

    /**
     * Gets array of all "measureGroup" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroup[] getMeasureGroupArray();

    /**
     * Gets ith "measureGroup" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroup getMeasureGroupArray(int i);

    /**
     * Returns number of "measureGroup" element
     */
    int sizeOfMeasureGroupArray();

    /**
     * Sets array of all "measureGroup" element
     */
    void setMeasureGroupArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroup[] measureGroupArray);

    /**
     * Sets ith "measureGroup" element
     */
    void setMeasureGroupArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroup measureGroup);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "measureGroup" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroup insertNewMeasureGroup(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "measureGroup" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroup addNewMeasureGroup();

    /**
     * Removes the ith "measureGroup" element
     */
    void removeMeasureGroup(int i);

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
