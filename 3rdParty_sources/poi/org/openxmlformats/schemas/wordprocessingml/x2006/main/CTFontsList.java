/*
 * XML Type:  CT_FontsList
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FontsList(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFontsList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfontslist2dd0type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "font" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont> getFontList();

    /**
     * Gets array of all "font" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont[] getFontArray();

    /**
     * Gets ith "font" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont getFontArray(int i);

    /**
     * Returns number of "font" element
     */
    int sizeOfFontArray();

    /**
     * Sets array of all "font" element
     */
    void setFontArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont[] fontArray);

    /**
     * Sets ith "font" element
     */
    void setFontArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont font);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "font" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont insertNewFont(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "font" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont addNewFont();

    /**
     * Removes the ith "font" element
     */
    void removeFont(int i);
}
