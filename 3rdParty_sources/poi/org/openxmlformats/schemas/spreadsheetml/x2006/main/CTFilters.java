/*
 * XML Type:  CT_Filters
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Filters(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFilters extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfiltersb69atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "filter" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter> getFilterList();

    /**
     * Gets array of all "filter" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter[] getFilterArray();

    /**
     * Gets ith "filter" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter getFilterArray(int i);

    /**
     * Returns number of "filter" element
     */
    int sizeOfFilterArray();

    /**
     * Sets array of all "filter" element
     */
    void setFilterArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter[] filterArray);

    /**
     * Sets ith "filter" element
     */
    void setFilterArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter filter);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "filter" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter insertNewFilter(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "filter" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter addNewFilter();

    /**
     * Removes the ith "filter" element
     */
    void removeFilter(int i);

    /**
     * Gets a List of "dateGroupItem" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem> getDateGroupItemList();

    /**
     * Gets array of all "dateGroupItem" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem[] getDateGroupItemArray();

    /**
     * Gets ith "dateGroupItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem getDateGroupItemArray(int i);

    /**
     * Returns number of "dateGroupItem" element
     */
    int sizeOfDateGroupItemArray();

    /**
     * Sets array of all "dateGroupItem" element
     */
    void setDateGroupItemArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem[] dateGroupItemArray);

    /**
     * Sets ith "dateGroupItem" element
     */
    void setDateGroupItemArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem dateGroupItem);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "dateGroupItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem insertNewDateGroupItem(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "dateGroupItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem addNewDateGroupItem();

    /**
     * Removes the ith "dateGroupItem" element
     */
    void removeDateGroupItem(int i);

    /**
     * Gets the "blank" attribute
     */
    boolean getBlank();

    /**
     * Gets (as xml) the "blank" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetBlank();

    /**
     * True if has "blank" attribute
     */
    boolean isSetBlank();

    /**
     * Sets the "blank" attribute
     */
    void setBlank(boolean blank);

    /**
     * Sets (as xml) the "blank" attribute
     */
    void xsetBlank(org.apache.xmlbeans.XmlBoolean blank);

    /**
     * Unsets the "blank" attribute
     */
    void unsetBlank();

    /**
     * Gets the "calendarType" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType.Enum getCalendarType();

    /**
     * Gets (as xml) the "calendarType" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType xgetCalendarType();

    /**
     * True if has "calendarType" attribute
     */
    boolean isSetCalendarType();

    /**
     * Sets the "calendarType" attribute
     */
    void setCalendarType(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType.Enum calendarType);

    /**
     * Sets (as xml) the "calendarType" attribute
     */
    void xsetCalendarType(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType calendarType);

    /**
     * Unsets the "calendarType" attribute
     */
    void unsetCalendarType();
}
