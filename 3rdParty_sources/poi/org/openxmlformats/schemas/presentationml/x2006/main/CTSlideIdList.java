/*
 * XML Type:  CT_SlideIdList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SlideIdList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSlideIdList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctslideidlist70a5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "sldId" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry> getSldIdList();

    /**
     * Gets array of all "sldId" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry[] getSldIdArray();

    /**
     * Gets ith "sldId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry getSldIdArray(int i);

    /**
     * Returns number of "sldId" element
     */
    int sizeOfSldIdArray();

    /**
     * Sets array of all "sldId" element
     */
    void setSldIdArray(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry[] sldIdArray);

    /**
     * Sets ith "sldId" element
     */
    void setSldIdArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry sldId);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sldId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry insertNewSldId(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "sldId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry addNewSldId();

    /**
     * Removes the ith "sldId" element
     */
    void removeSldId(int i);
}
