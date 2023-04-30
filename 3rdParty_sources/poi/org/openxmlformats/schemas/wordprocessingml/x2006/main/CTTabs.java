/*
 * XML Type:  CT_Tabs
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Tabs(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTabs extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttabsa2aatype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "tab" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop> getTabList();

    /**
     * Gets array of all "tab" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop[] getTabArray();

    /**
     * Gets ith "tab" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop getTabArray(int i);

    /**
     * Returns number of "tab" element
     */
    int sizeOfTabArray();

    /**
     * Sets array of all "tab" element
     */
    void setTabArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop[] tabArray);

    /**
     * Sets ith "tab" element
     */
    void setTabArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop tab);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tab" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop insertNewTab(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "tab" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop addNewTab();

    /**
     * Removes the ith "tab" element
     */
    void removeTab(int i);
}
