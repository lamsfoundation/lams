/*
 * XML Type:  CT_Path2DList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Path2DList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPath2DList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpath2dlistb010type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "path" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D> getPathList();

    /**
     * Gets array of all "path" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D[] getPathArray();

    /**
     * Gets ith "path" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D getPathArray(int i);

    /**
     * Returns number of "path" element
     */
    int sizeOfPathArray();

    /**
     * Sets array of all "path" element
     */
    void setPathArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D[] pathArray);

    /**
     * Sets ith "path" element
     */
    void setPathArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D path);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "path" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D insertNewPath(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "path" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D addNewPath();

    /**
     * Removes the ith "path" element
     */
    void removePath(int i);
}
