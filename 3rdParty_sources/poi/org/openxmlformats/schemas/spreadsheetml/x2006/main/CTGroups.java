/*
 * XML Type:  CT_Groups
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroups
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Groups(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGroups extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroups> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgroups7cddtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "group" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLevelGroup> getGroupList();

    /**
     * Gets array of all "group" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLevelGroup[] getGroupArray();

    /**
     * Gets ith "group" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLevelGroup getGroupArray(int i);

    /**
     * Returns number of "group" element
     */
    int sizeOfGroupArray();

    /**
     * Sets array of all "group" element
     */
    void setGroupArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLevelGroup[] groupArray);

    /**
     * Sets ith "group" element
     */
    void setGroupArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLevelGroup group);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "group" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLevelGroup insertNewGroup(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "group" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLevelGroup addNewGroup();

    /**
     * Removes the ith "group" element
     */
    void removeGroup(int i);

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
