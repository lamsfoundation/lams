/*
 * XML Type:  CT_VolTypes
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTypes
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_VolTypes(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTVolTypes extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTypes> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctvoltypes1eaftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "volType" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolType> getVolTypeList();

    /**
     * Gets array of all "volType" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolType[] getVolTypeArray();

    /**
     * Gets ith "volType" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolType getVolTypeArray(int i);

    /**
     * Returns number of "volType" element
     */
    int sizeOfVolTypeArray();

    /**
     * Sets array of all "volType" element
     */
    void setVolTypeArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolType[] volTypeArray);

    /**
     * Sets ith "volType" element
     */
    void setVolTypeArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolType volType);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "volType" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolType insertNewVolType(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "volType" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolType addNewVolType();

    /**
     * Removes the ith "volType" element
     */
    void removeVolType(int i);

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}
