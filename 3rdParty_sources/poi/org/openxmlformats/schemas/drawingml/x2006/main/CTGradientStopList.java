/*
 * XML Type:  CT_GradientStopList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GradientStopList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGradientStopList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgradientstoplist7eabtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "gs" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop> getGsList();

    /**
     * Gets array of all "gs" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop[] getGsArray();

    /**
     * Gets ith "gs" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop getGsArray(int i);

    /**
     * Returns number of "gs" element
     */
    int sizeOfGsArray();

    /**
     * Sets array of all "gs" element
     */
    void setGsArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop[] gsArray);

    /**
     * Sets ith "gs" element
     */
    void setGsArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop gs);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gs" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop insertNewGs(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "gs" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop addNewGs();

    /**
     * Removes the ith "gs" element
     */
    void removeGs(int i);
}
