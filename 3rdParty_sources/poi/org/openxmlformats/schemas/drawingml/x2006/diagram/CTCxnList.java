/*
 * XML Type:  CT_CxnList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CxnList(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTCxnList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcxnlist1564type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cxn" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn> getCxnList();

    /**
     * Gets array of all "cxn" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn[] getCxnArray();

    /**
     * Gets ith "cxn" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn getCxnArray(int i);

    /**
     * Returns number of "cxn" element
     */
    int sizeOfCxnArray();

    /**
     * Sets array of all "cxn" element
     */
    void setCxnArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn[] cxnArray);

    /**
     * Sets ith "cxn" element
     */
    void setCxnArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn cxn);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cxn" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn insertNewCxn(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cxn" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn addNewCxn();

    /**
     * Removes the ith "cxn" element
     */
    void removeCxn(int i);
}
