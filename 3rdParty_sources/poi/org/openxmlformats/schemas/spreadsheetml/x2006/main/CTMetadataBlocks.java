/*
 * XML Type:  CT_MetadataBlocks
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MetadataBlocks(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMetadataBlocks extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmetadatablocks205etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "bk" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlock> getBkList();

    /**
     * Gets array of all "bk" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlock[] getBkArray();

    /**
     * Gets ith "bk" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlock getBkArray(int i);

    /**
     * Returns number of "bk" element
     */
    int sizeOfBkArray();

    /**
     * Sets array of all "bk" element
     */
    void setBkArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlock[] bkArray);

    /**
     * Sets ith "bk" element
     */
    void setBkArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlock bk);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bk" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlock insertNewBk(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "bk" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlock addNewBk();

    /**
     * Removes the ith "bk" element
     */
    void removeBk(int i);

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
