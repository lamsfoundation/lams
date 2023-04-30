/*
 * XML Type:  CT_ExtensionList
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ExtensionList(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTExtensionList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctextensionlist8d5ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "ext" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtension> getExtList();

    /**
     * Gets array of all "ext" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtension[] getExtArray();

    /**
     * Gets ith "ext" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtension getExtArray(int i);

    /**
     * Returns number of "ext" element
     */
    int sizeOfExtArray();

    /**
     * Sets array of all "ext" element
     */
    void setExtArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtension[] extArray);

    /**
     * Sets ith "ext" element
     */
    void setExtArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtension ext);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ext" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtension insertNewExt(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ext" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtension addNewExt();

    /**
     * Removes the ith "ext" element
     */
    void removeExt(int i);
}
