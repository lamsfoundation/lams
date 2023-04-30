/*
 * XML Type:  CT_Categories
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategories
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Categories(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTCategories extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategories> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcategories6955type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cat" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategory> getCatList();

    /**
     * Gets array of all "cat" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategory[] getCatArray();

    /**
     * Gets ith "cat" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategory getCatArray(int i);

    /**
     * Returns number of "cat" element
     */
    int sizeOfCatArray();

    /**
     * Sets array of all "cat" element
     */
    void setCatArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategory[] catArray);

    /**
     * Sets ith "cat" element
     */
    void setCatArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategory cat);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cat" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategory insertNewCat(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cat" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategory addNewCat();

    /**
     * Removes the ith "cat" element
     */
    void removeCat(int i);
}
