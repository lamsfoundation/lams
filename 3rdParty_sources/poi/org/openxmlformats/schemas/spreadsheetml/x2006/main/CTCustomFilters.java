/*
 * XML Type:  CT_CustomFilters
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilters
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CustomFilters(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCustomFilters extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilters> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcustomfilters58e9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "customFilter" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter> getCustomFilterList();

    /**
     * Gets array of all "customFilter" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter[] getCustomFilterArray();

    /**
     * Gets ith "customFilter" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter getCustomFilterArray(int i);

    /**
     * Returns number of "customFilter" element
     */
    int sizeOfCustomFilterArray();

    /**
     * Sets array of all "customFilter" element
     */
    void setCustomFilterArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter[] customFilterArray);

    /**
     * Sets ith "customFilter" element
     */
    void setCustomFilterArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter customFilter);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customFilter" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter insertNewCustomFilter(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "customFilter" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter addNewCustomFilter();

    /**
     * Removes the ith "customFilter" element
     */
    void removeCustomFilter(int i);

    /**
     * Gets the "and" attribute
     */
    boolean getAnd();

    /**
     * Gets (as xml) the "and" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAnd();

    /**
     * True if has "and" attribute
     */
    boolean isSetAnd();

    /**
     * Sets the "and" attribute
     */
    void setAnd(boolean and);

    /**
     * Sets (as xml) the "and" attribute
     */
    void xsetAnd(org.apache.xmlbeans.XmlBoolean and);

    /**
     * Unsets the "and" attribute
     */
    void unsetAnd();
}
