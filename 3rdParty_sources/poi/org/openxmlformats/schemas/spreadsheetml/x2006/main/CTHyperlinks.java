/*
 * XML Type:  CT_Hyperlinks
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlinks
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Hyperlinks(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTHyperlinks extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlinks> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cthyperlinks6416type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "hyperlink" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink> getHyperlinkList();

    /**
     * Gets array of all "hyperlink" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink[] getHyperlinkArray();

    /**
     * Gets ith "hyperlink" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink getHyperlinkArray(int i);

    /**
     * Returns number of "hyperlink" element
     */
    int sizeOfHyperlinkArray();

    /**
     * Sets array of all "hyperlink" element
     */
    void setHyperlinkArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink[] hyperlinkArray);

    /**
     * Sets ith "hyperlink" element
     */
    void setHyperlinkArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink hyperlink);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "hyperlink" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink insertNewHyperlink(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "hyperlink" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink addNewHyperlink();

    /**
     * Removes the ith "hyperlink" element
     */
    void removeHyperlink(int i);
}
