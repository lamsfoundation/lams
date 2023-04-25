/*
 * XML Type:  CT_Sets
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSets
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Sets(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSets extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSets> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsets725atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "set" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSet> getSetList();

    /**
     * Gets array of all "set" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSet[] getSetArray();

    /**
     * Gets ith "set" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSet getSetArray(int i);

    /**
     * Returns number of "set" element
     */
    int sizeOfSetArray();

    /**
     * Sets array of all "set" element
     */
    void setSetArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSet[] setArray);

    /**
     * Sets ith "set" element
     */
    void setSetArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSet set);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "set" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSet insertNewSet(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "set" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSet addNewSet();

    /**
     * Removes the ith "set" element
     */
    void removeSet(int i);

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
