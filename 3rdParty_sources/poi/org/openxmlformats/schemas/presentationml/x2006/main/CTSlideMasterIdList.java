/*
 * XML Type:  CT_SlideMasterIdList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SlideMasterIdList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSlideMasterIdList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctslidemasteridlist0b63type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "sldMasterId" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry> getSldMasterIdList();

    /**
     * Gets array of all "sldMasterId" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry[] getSldMasterIdArray();

    /**
     * Gets ith "sldMasterId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry getSldMasterIdArray(int i);

    /**
     * Returns number of "sldMasterId" element
     */
    int sizeOfSldMasterIdArray();

    /**
     * Sets array of all "sldMasterId" element
     */
    void setSldMasterIdArray(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry[] sldMasterIdArray);

    /**
     * Sets ith "sldMasterId" element
     */
    void setSldMasterIdArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry sldMasterId);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sldMasterId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry insertNewSldMasterId(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "sldMasterId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry addNewSldMasterId();

    /**
     * Removes the ith "sldMasterId" element
     */
    void removeSldMasterId(int i);
}
