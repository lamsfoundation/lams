/*
 * XML Type:  CT_VolType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_VolType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTVolType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctvoltype1acctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "main" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain> getMainList();

    /**
     * Gets array of all "main" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain[] getMainArray();

    /**
     * Gets ith "main" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain getMainArray(int i);

    /**
     * Returns number of "main" element
     */
    int sizeOfMainArray();

    /**
     * Sets array of all "main" element
     */
    void setMainArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain[] mainArray);

    /**
     * Sets ith "main" element
     */
    void setMainArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain main);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "main" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain insertNewMain(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "main" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain addNewMain();

    /**
     * Removes the ith "main" element
     */
    void removeMain(int i);

    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType xgetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType type);
}
