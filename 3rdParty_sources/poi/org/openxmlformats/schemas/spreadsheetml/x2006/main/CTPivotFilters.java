/*
 * XML Type:  CT_PivotFilters
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilters
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PivotFilters(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPivotFilters extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilters> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpivotfilters1462type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "filter" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilter> getFilterList();

    /**
     * Gets array of all "filter" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilter[] getFilterArray();

    /**
     * Gets ith "filter" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilter getFilterArray(int i);

    /**
     * Returns number of "filter" element
     */
    int sizeOfFilterArray();

    /**
     * Sets array of all "filter" element
     */
    void setFilterArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilter[] filterArray);

    /**
     * Sets ith "filter" element
     */
    void setFilterArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilter filter);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "filter" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilter insertNewFilter(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "filter" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilter addNewFilter();

    /**
     * Removes the ith "filter" element
     */
    void removeFilter(int i);

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
