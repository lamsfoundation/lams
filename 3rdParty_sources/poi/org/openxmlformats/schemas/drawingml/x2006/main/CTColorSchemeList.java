/*
 * XML Type:  CT_ColorSchemeList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ColorSchemeList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTColorSchemeList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcolorschemelist8f57type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "extraClrScheme" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping> getExtraClrSchemeList();

    /**
     * Gets array of all "extraClrScheme" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping[] getExtraClrSchemeArray();

    /**
     * Gets ith "extraClrScheme" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping getExtraClrSchemeArray(int i);

    /**
     * Returns number of "extraClrScheme" element
     */
    int sizeOfExtraClrSchemeArray();

    /**
     * Sets array of all "extraClrScheme" element
     */
    void setExtraClrSchemeArray(org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping[] extraClrSchemeArray);

    /**
     * Sets ith "extraClrScheme" element
     */
    void setExtraClrSchemeArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping extraClrScheme);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "extraClrScheme" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping insertNewExtraClrScheme(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "extraClrScheme" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping addNewExtraClrScheme();

    /**
     * Removes the ith "extraClrScheme" element
     */
    void removeExtraClrScheme(int i);
}
