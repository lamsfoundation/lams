/*
 * XML Type:  CT_EmbeddedFontList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_EmbeddedFontList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTEmbeddedFontList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctembeddedfontlist240etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "embeddedFont" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry> getEmbeddedFontList();

    /**
     * Gets array of all "embeddedFont" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry[] getEmbeddedFontArray();

    /**
     * Gets ith "embeddedFont" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry getEmbeddedFontArray(int i);

    /**
     * Returns number of "embeddedFont" element
     */
    int sizeOfEmbeddedFontArray();

    /**
     * Sets array of all "embeddedFont" element
     */
    void setEmbeddedFontArray(org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry[] embeddedFontArray);

    /**
     * Sets ith "embeddedFont" element
     */
    void setEmbeddedFontArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry embeddedFont);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "embeddedFont" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry insertNewEmbeddedFont(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "embeddedFont" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry addNewEmbeddedFont();

    /**
     * Removes the ith "embeddedFont" element
     */
    void removeEmbeddedFont(int i);
}
