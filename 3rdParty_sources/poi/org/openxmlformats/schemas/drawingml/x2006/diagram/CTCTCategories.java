/*
 * XML Type:  CT_CTCategories
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategories
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CTCategories(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTCTCategories extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategories> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctctcategories7c84type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cat" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategory> getCatList();

    /**
     * Gets array of all "cat" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategory[] getCatArray();

    /**
     * Gets ith "cat" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategory getCatArray(int i);

    /**
     * Returns number of "cat" element
     */
    int sizeOfCatArray();

    /**
     * Sets array of all "cat" element
     */
    void setCatArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategory[] catArray);

    /**
     * Sets ith "cat" element
     */
    void setCatArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategory cat);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cat" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategory insertNewCat(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cat" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategory addNewCat();

    /**
     * Removes the ith "cat" element
     */
    void removeCat(int i);
}
