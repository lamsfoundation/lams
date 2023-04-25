/*
 * XML Type:  CT_Path2DCubicBezierTo
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Path2DCubicBezierTo(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPath2DCubicBezierTo extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpath2dcubicbezierto5a1etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "pt" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D> getPtList();

    /**
     * Gets array of all "pt" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D[] getPtArray();

    /**
     * Gets ith "pt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D getPtArray(int i);

    /**
     * Returns number of "pt" element
     */
    int sizeOfPtArray();

    /**
     * Sets array of all "pt" element
     */
    void setPtArray(org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D[] ptArray);

    /**
     * Sets ith "pt" element
     */
    void setPtArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D pt);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D insertNewPt(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "pt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D addNewPt();

    /**
     * Removes the ith "pt" element
     */
    void removePt(int i);
}
