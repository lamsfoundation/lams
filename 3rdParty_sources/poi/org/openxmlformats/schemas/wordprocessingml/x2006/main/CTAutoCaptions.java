/*
 * XML Type:  CT_AutoCaptions
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AutoCaptions(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAutoCaptions extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctautocaptions89cctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "autoCaption" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption> getAutoCaptionList();

    /**
     * Gets array of all "autoCaption" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption[] getAutoCaptionArray();

    /**
     * Gets ith "autoCaption" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption getAutoCaptionArray(int i);

    /**
     * Returns number of "autoCaption" element
     */
    int sizeOfAutoCaptionArray();

    /**
     * Sets array of all "autoCaption" element
     */
    void setAutoCaptionArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption[] autoCaptionArray);

    /**
     * Sets ith "autoCaption" element
     */
    void setAutoCaptionArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption autoCaption);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "autoCaption" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption insertNewAutoCaption(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "autoCaption" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption addNewAutoCaption();

    /**
     * Removes the ith "autoCaption" element
     */
    void removeAutoCaption(int i);
}
