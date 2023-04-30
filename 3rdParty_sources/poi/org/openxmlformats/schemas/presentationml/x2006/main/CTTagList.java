/*
 * XML Type:  CT_TagList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTagList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TagList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTagList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTagList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttaglist6477type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "tag" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag> getTagList();

    /**
     * Gets array of all "tag" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag[] getTagArray();

    /**
     * Gets ith "tag" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag getTagArray(int i);

    /**
     * Returns number of "tag" element
     */
    int sizeOfTagArray();

    /**
     * Sets array of all "tag" element
     */
    void setTagArray(org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag[] tagArray);

    /**
     * Sets ith "tag" element
     */
    void setTagArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag tag);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tag" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag insertNewTag(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "tag" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag addNewTag();

    /**
     * Removes the ith "tag" element
     */
    void removeTag(int i);
}
