/*
 * XML Type:  CT_Controls
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControls
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Controls(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTControls extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControls> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcontrols75fftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "control" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControl> getControlList();

    /**
     * Gets array of all "control" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControl[] getControlArray();

    /**
     * Gets ith "control" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControl getControlArray(int i);

    /**
     * Returns number of "control" element
     */
    int sizeOfControlArray();

    /**
     * Sets array of all "control" element
     */
    void setControlArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControl[] controlArray);

    /**
     * Sets ith "control" element
     */
    void setControlArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControl control);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "control" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControl insertNewControl(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "control" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControl addNewControl();

    /**
     * Removes the ith "control" element
     */
    void removeControl(int i);
}
