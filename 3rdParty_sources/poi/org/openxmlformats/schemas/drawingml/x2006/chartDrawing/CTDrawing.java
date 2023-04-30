/*
 * XML Type:  CT_Drawing
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chartDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chartDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Drawing(@http://schemas.openxmlformats.org/drawingml/2006/chartDrawing).
 *
 * This is a complex type.
 */
public interface CTDrawing extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdrawingfb76type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "relSizeAnchor" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor> getRelSizeAnchorList();

    /**
     * Gets array of all "relSizeAnchor" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor[] getRelSizeAnchorArray();

    /**
     * Gets ith "relSizeAnchor" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor getRelSizeAnchorArray(int i);

    /**
     * Returns number of "relSizeAnchor" element
     */
    int sizeOfRelSizeAnchorArray();

    /**
     * Sets array of all "relSizeAnchor" element
     */
    void setRelSizeAnchorArray(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor[] relSizeAnchorArray);

    /**
     * Sets ith "relSizeAnchor" element
     */
    void setRelSizeAnchorArray(int i, org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor relSizeAnchor);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "relSizeAnchor" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor insertNewRelSizeAnchor(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "relSizeAnchor" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor addNewRelSizeAnchor();

    /**
     * Removes the ith "relSizeAnchor" element
     */
    void removeRelSizeAnchor(int i);

    /**
     * Gets a List of "absSizeAnchor" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor> getAbsSizeAnchorList();

    /**
     * Gets array of all "absSizeAnchor" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor[] getAbsSizeAnchorArray();

    /**
     * Gets ith "absSizeAnchor" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor getAbsSizeAnchorArray(int i);

    /**
     * Returns number of "absSizeAnchor" element
     */
    int sizeOfAbsSizeAnchorArray();

    /**
     * Sets array of all "absSizeAnchor" element
     */
    void setAbsSizeAnchorArray(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor[] absSizeAnchorArray);

    /**
     * Sets ith "absSizeAnchor" element
     */
    void setAbsSizeAnchorArray(int i, org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor absSizeAnchor);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "absSizeAnchor" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor insertNewAbsSizeAnchor(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "absSizeAnchor" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor addNewAbsSizeAnchor();

    /**
     * Removes the ith "absSizeAnchor" element
     */
    void removeAbsSizeAnchor(int i);
}
