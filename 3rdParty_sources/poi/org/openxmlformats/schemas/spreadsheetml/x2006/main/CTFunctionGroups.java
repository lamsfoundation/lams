/*
 * XML Type:  CT_FunctionGroups
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroups
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FunctionGroups(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFunctionGroups extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroups> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfunctiongroupsbfd5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "functionGroup" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup> getFunctionGroupList();

    /**
     * Gets array of all "functionGroup" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup[] getFunctionGroupArray();

    /**
     * Gets ith "functionGroup" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup getFunctionGroupArray(int i);

    /**
     * Returns number of "functionGroup" element
     */
    int sizeOfFunctionGroupArray();

    /**
     * Sets array of all "functionGroup" element
     */
    void setFunctionGroupArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup[] functionGroupArray);

    /**
     * Sets ith "functionGroup" element
     */
    void setFunctionGroupArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup functionGroup);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "functionGroup" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup insertNewFunctionGroup(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "functionGroup" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup addNewFunctionGroup();

    /**
     * Removes the ith "functionGroup" element
     */
    void removeFunctionGroup(int i);

    /**
     * Gets the "builtInGroupCount" attribute
     */
    long getBuiltInGroupCount();

    /**
     * Gets (as xml) the "builtInGroupCount" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetBuiltInGroupCount();

    /**
     * True if has "builtInGroupCount" attribute
     */
    boolean isSetBuiltInGroupCount();

    /**
     * Sets the "builtInGroupCount" attribute
     */
    void setBuiltInGroupCount(long builtInGroupCount);

    /**
     * Sets (as xml) the "builtInGroupCount" attribute
     */
    void xsetBuiltInGroupCount(org.apache.xmlbeans.XmlUnsignedInt builtInGroupCount);

    /**
     * Unsets the "builtInGroupCount" attribute
     */
    void unsetBuiltInGroupCount();
}
