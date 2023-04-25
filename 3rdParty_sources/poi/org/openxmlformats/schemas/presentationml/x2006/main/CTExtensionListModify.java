/*
 * XML Type:  CT_ExtensionListModify
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ExtensionListModify(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTExtensionListModify extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctextensionlistmodify8c58type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "ext" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTExtension> getExtList();

    /**
     * Gets array of all "ext" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtension[] getExtArray();

    /**
     * Gets ith "ext" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtension getExtArray(int i);

    /**
     * Returns number of "ext" element
     */
    int sizeOfExtArray();

    /**
     * Sets array of all "ext" element
     */
    void setExtArray(org.openxmlformats.schemas.presentationml.x2006.main.CTExtension[] extArray);

    /**
     * Sets ith "ext" element
     */
    void setExtArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTExtension ext);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ext" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtension insertNewExt(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ext" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtension addNewExt();

    /**
     * Removes the ith "ext" element
     */
    void removeExt(int i);

    /**
     * Gets the "mod" attribute
     */
    boolean getMod();

    /**
     * Gets (as xml) the "mod" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetMod();

    /**
     * True if has "mod" attribute
     */
    boolean isSetMod();

    /**
     * Sets the "mod" attribute
     */
    void setMod(boolean mod);

    /**
     * Sets (as xml) the "mod" attribute
     */
    void xsetMod(org.apache.xmlbeans.XmlBoolean mod);

    /**
     * Unsets the "mod" attribute
     */
    void unsetMod();
}
