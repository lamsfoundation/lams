/*
 * XML Type:  CT_GeomGuideList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GeomGuideList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGeomGuideList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgeomguidelist364ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "gd" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide> getGdList();

    /**
     * Gets array of all "gd" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide[] getGdArray();

    /**
     * Gets ith "gd" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide getGdArray(int i);

    /**
     * Returns number of "gd" element
     */
    int sizeOfGdArray();

    /**
     * Sets array of all "gd" element
     */
    void setGdArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide[] gdArray);

    /**
     * Sets ith "gd" element
     */
    void setGdArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide gd);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gd" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide insertNewGd(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "gd" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide addNewGd();

    /**
     * Removes the ith "gd" element
     */
    void removeGd(int i);
}
