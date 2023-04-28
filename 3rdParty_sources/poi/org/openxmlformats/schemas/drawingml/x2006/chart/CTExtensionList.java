/*
 * XML Type:  CT_ExtensionList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ExtensionList(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTExtensionList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctextensionlist7389type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "ext" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension> getExtList();

    /**
     * Gets array of all "ext" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension[] getExtArray();

    /**
     * Gets ith "ext" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension getExtArray(int i);

    /**
     * Returns number of "ext" element
     */
    int sizeOfExtArray();

    /**
     * Sets array of all "ext" element
     */
    void setExtArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension[] extArray);

    /**
     * Sets ith "ext" element
     */
    void setExtArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension ext);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ext" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension insertNewExt(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ext" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension addNewExt();

    /**
     * Removes the ith "ext" element
     */
    void removeExt(int i);
}
