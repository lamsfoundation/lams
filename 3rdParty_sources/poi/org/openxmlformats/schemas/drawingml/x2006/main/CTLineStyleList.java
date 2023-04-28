/*
 * XML Type:  CT_LineStyleList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_LineStyleList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTLineStyleList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlinestylelist510ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "ln" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties> getLnList();

    /**
     * Gets array of all "ln" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties[] getLnArray();

    /**
     * Gets ith "ln" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getLnArray(int i);

    /**
     * Returns number of "ln" element
     */
    int sizeOfLnArray();

    /**
     * Sets array of all "ln" element
     */
    void setLnArray(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties[] lnArray);

    /**
     * Sets ith "ln" element
     */
    void setLnArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties ln);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ln" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties insertNewLn(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ln" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewLn();

    /**
     * Removes the ith "ln" element
     */
    void removeLn(int i);
}
