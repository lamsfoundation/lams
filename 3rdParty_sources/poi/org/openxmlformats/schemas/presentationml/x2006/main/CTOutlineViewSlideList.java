/*
 * XML Type:  CT_OutlineViewSlideList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OutlineViewSlideList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOutlineViewSlideList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctoutlineviewslideliste67dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "sld" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideEntry> getSldList();

    /**
     * Gets array of all "sld" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideEntry[] getSldArray();

    /**
     * Gets ith "sld" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideEntry getSldArray(int i);

    /**
     * Returns number of "sld" element
     */
    int sizeOfSldArray();

    /**
     * Sets array of all "sld" element
     */
    void setSldArray(org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideEntry[] sldArray);

    /**
     * Sets ith "sld" element
     */
    void setSldArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideEntry sld);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sld" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideEntry insertNewSld(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "sld" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideEntry addNewSld();

    /**
     * Removes the ith "sld" element
     */
    void removeSld(int i);
}
