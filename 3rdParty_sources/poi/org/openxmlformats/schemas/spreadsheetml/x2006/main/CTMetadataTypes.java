/*
 * XML Type:  CT_MetadataTypes
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataTypes
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MetadataTypes(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMetadataTypes extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataTypes> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmetadatatypes3c29type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "metadataType" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataType> getMetadataTypeList();

    /**
     * Gets array of all "metadataType" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataType[] getMetadataTypeArray();

    /**
     * Gets ith "metadataType" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataType getMetadataTypeArray(int i);

    /**
     * Returns number of "metadataType" element
     */
    int sizeOfMetadataTypeArray();

    /**
     * Sets array of all "metadataType" element
     */
    void setMetadataTypeArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataType[] metadataTypeArray);

    /**
     * Sets ith "metadataType" element
     */
    void setMetadataTypeArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataType metadataType);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "metadataType" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataType insertNewMetadataType(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "metadataType" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataType addNewMetadataType();

    /**
     * Removes the ith "metadataType" element
     */
    void removeMetadataType(int i);

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
