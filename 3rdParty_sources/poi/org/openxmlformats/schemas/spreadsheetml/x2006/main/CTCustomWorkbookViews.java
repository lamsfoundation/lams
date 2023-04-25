/*
 * XML Type:  CT_CustomWorkbookViews
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookViews
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CustomWorkbookViews(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCustomWorkbookViews extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookViews> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcustomworkbookviewse942type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "customWorkbookView" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookView> getCustomWorkbookViewList();

    /**
     * Gets array of all "customWorkbookView" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookView[] getCustomWorkbookViewArray();

    /**
     * Gets ith "customWorkbookView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookView getCustomWorkbookViewArray(int i);

    /**
     * Returns number of "customWorkbookView" element
     */
    int sizeOfCustomWorkbookViewArray();

    /**
     * Sets array of all "customWorkbookView" element
     */
    void setCustomWorkbookViewArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookView[] customWorkbookViewArray);

    /**
     * Sets ith "customWorkbookView" element
     */
    void setCustomWorkbookViewArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookView customWorkbookView);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customWorkbookView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookView insertNewCustomWorkbookView(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "customWorkbookView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookView addNewCustomWorkbookView();

    /**
     * Removes the ith "customWorkbookView" element
     */
    void removeCustomWorkbookView(int i);
}
