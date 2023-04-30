/*
 * XML Type:  CT_TextTabStopList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextTabStopList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTextTabStopList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttexttabstoplistf539type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "tab" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop> getTabList();

    /**
     * Gets array of all "tab" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop[] getTabArray();

    /**
     * Gets ith "tab" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop getTabArray(int i);

    /**
     * Returns number of "tab" element
     */
    int sizeOfTabArray();

    /**
     * Sets array of all "tab" element
     */
    void setTabArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop[] tabArray);

    /**
     * Sets ith "tab" element
     */
    void setTabArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop tab);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tab" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop insertNewTab(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "tab" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop addNewTab();

    /**
     * Removes the ith "tab" element
     */
    void removeTab(int i);
}
