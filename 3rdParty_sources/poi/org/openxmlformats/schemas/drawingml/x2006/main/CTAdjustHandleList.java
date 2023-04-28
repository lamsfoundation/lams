/*
 * XML Type:  CT_AdjustHandleList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AdjustHandleList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAdjustHandleList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctadjusthandlelistfdb0type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "ahXY" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle> getAhXYList();

    /**
     * Gets array of all "ahXY" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle[] getAhXYArray();

    /**
     * Gets ith "ahXY" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle getAhXYArray(int i);

    /**
     * Returns number of "ahXY" element
     */
    int sizeOfAhXYArray();

    /**
     * Sets array of all "ahXY" element
     */
    void setAhXYArray(org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle[] ahXYArray);

    /**
     * Sets ith "ahXY" element
     */
    void setAhXYArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle ahXY);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ahXY" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle insertNewAhXY(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ahXY" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle addNewAhXY();

    /**
     * Removes the ith "ahXY" element
     */
    void removeAhXY(int i);

    /**
     * Gets a List of "ahPolar" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle> getAhPolarList();

    /**
     * Gets array of all "ahPolar" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle[] getAhPolarArray();

    /**
     * Gets ith "ahPolar" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle getAhPolarArray(int i);

    /**
     * Returns number of "ahPolar" element
     */
    int sizeOfAhPolarArray();

    /**
     * Sets array of all "ahPolar" element
     */
    void setAhPolarArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle[] ahPolarArray);

    /**
     * Sets ith "ahPolar" element
     */
    void setAhPolarArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle ahPolar);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ahPolar" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle insertNewAhPolar(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ahPolar" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle addNewAhPolar();

    /**
     * Removes the ith "ahPolar" element
     */
    void removeAhPolar(int i);
}
