/*
 * XML Type:  CT_MeasureDimensionMaps
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMaps
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MeasureDimensionMaps(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMeasureDimensionMaps extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMaps> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmeasuredimensionmapse2e8type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "map" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMap> getMapList();

    /**
     * Gets array of all "map" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMap[] getMapArray();

    /**
     * Gets ith "map" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMap getMapArray(int i);

    /**
     * Returns number of "map" element
     */
    int sizeOfMapArray();

    /**
     * Sets array of all "map" element
     */
    void setMapArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMap[] mapArray);

    /**
     * Sets ith "map" element
     */
    void setMapArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMap map);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "map" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMap insertNewMap(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "map" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMap addNewMap();

    /**
     * Removes the ith "map" element
     */
    void removeMap(int i);

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
