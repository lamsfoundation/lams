/*
 * XML Type:  CT_GuideList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTGuideList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GuideList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGuideList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTGuideList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctguidelist1f95type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "guide" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTGuide> getGuideList();

    /**
     * Gets array of all "guide" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGuide[] getGuideArray();

    /**
     * Gets ith "guide" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGuide getGuideArray(int i);

    /**
     * Returns number of "guide" element
     */
    int sizeOfGuideArray();

    /**
     * Sets array of all "guide" element
     */
    void setGuideArray(org.openxmlformats.schemas.presentationml.x2006.main.CTGuide[] guideArray);

    /**
     * Sets ith "guide" element
     */
    void setGuideArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTGuide guide);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "guide" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGuide insertNewGuide(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "guide" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGuide addNewGuide();

    /**
     * Removes the ith "guide" element
     */
    void removeGuide(int i);
}
