/*
 * XML Type:  CT_DashStopList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTDashStopList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DashStopList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDashStopList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTDashStopList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdashstoplist920dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "ds" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop> getDsList();

    /**
     * Gets array of all "ds" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop[] getDsArray();

    /**
     * Gets ith "ds" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop getDsArray(int i);

    /**
     * Returns number of "ds" element
     */
    int sizeOfDsArray();

    /**
     * Sets array of all "ds" element
     */
    void setDsArray(org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop[] dsArray);

    /**
     * Sets ith "ds" element
     */
    void setDsArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop ds);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ds" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop insertNewDs(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ds" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop addNewDs();

    /**
     * Removes the ith "ds" element
     */
    void removeDs(int i);
}
