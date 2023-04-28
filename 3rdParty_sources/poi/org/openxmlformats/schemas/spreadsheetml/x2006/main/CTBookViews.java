/*
 * XML Type:  CT_BookViews
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_BookViews(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBookViews extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctbookviewsb864type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "workbookView" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView> getWorkbookViewList();

    /**
     * Gets array of all "workbookView" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView[] getWorkbookViewArray();

    /**
     * Gets ith "workbookView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView getWorkbookViewArray(int i);

    /**
     * Returns number of "workbookView" element
     */
    int sizeOfWorkbookViewArray();

    /**
     * Sets array of all "workbookView" element
     */
    void setWorkbookViewArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView[] workbookViewArray);

    /**
     * Sets ith "workbookView" element
     */
    void setWorkbookViewArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView workbookView);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "workbookView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView insertNewWorkbookView(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "workbookView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView addNewWorkbookView();

    /**
     * Removes the ith "workbookView" element
     */
    void removeWorkbookView(int i);
}
