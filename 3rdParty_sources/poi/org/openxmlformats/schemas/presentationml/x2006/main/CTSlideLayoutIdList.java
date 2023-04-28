/*
 * XML Type:  CT_SlideLayoutIdList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SlideLayoutIdList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSlideLayoutIdList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctslidelayoutidlist939btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "sldLayoutId" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry> getSldLayoutIdList();

    /**
     * Gets array of all "sldLayoutId" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry[] getSldLayoutIdArray();

    /**
     * Gets ith "sldLayoutId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry getSldLayoutIdArray(int i);

    /**
     * Returns number of "sldLayoutId" element
     */
    int sizeOfSldLayoutIdArray();

    /**
     * Sets array of all "sldLayoutId" element
     */
    void setSldLayoutIdArray(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry[] sldLayoutIdArray);

    /**
     * Sets ith "sldLayoutId" element
     */
    void setSldLayoutIdArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry sldLayoutId);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sldLayoutId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry insertNewSldLayoutId(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "sldLayoutId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry addNewSldLayoutId();

    /**
     * Removes the ith "sldLayoutId" element
     */
    void removeSldLayoutId(int i);
}
