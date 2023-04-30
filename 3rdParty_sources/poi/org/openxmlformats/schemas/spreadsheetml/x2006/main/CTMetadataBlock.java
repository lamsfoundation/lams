/*
 * XML Type:  CT_MetadataBlock
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlock
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MetadataBlock(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMetadataBlock extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlock> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmetadatablocka73dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "rc" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord> getRcList();

    /**
     * Gets array of all "rc" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord[] getRcArray();

    /**
     * Gets ith "rc" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord getRcArray(int i);

    /**
     * Returns number of "rc" element
     */
    int sizeOfRcArray();

    /**
     * Sets array of all "rc" element
     */
    void setRcArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord[] rcArray);

    /**
     * Sets ith "rc" element
     */
    void setRcArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord rc);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rc" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord insertNewRc(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "rc" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord addNewRc();

    /**
     * Removes the ith "rc" element
     */
    void removeRc(int i);
}
