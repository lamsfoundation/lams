/*
 * XML Type:  CT_Scenario
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Scenario(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTScenario extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctscenariofe59type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "inputCells" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTInputCells> getInputCellsList();

    /**
     * Gets array of all "inputCells" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTInputCells[] getInputCellsArray();

    /**
     * Gets ith "inputCells" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTInputCells getInputCellsArray(int i);

    /**
     * Returns number of "inputCells" element
     */
    int sizeOfInputCellsArray();

    /**
     * Sets array of all "inputCells" element
     */
    void setInputCellsArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTInputCells[] inputCellsArray);

    /**
     * Sets ith "inputCells" element
     */
    void setInputCellsArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTInputCells inputCells);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "inputCells" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTInputCells insertNewInputCells(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "inputCells" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTInputCells addNewInputCells();

    /**
     * Removes the ith "inputCells" element
     */
    void removeInputCells(int i);

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name);

    /**
     * Gets the "locked" attribute
     */
    boolean getLocked();

    /**
     * Gets (as xml) the "locked" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetLocked();

    /**
     * True if has "locked" attribute
     */
    boolean isSetLocked();

    /**
     * Sets the "locked" attribute
     */
    void setLocked(boolean locked);

    /**
     * Sets (as xml) the "locked" attribute
     */
    void xsetLocked(org.apache.xmlbeans.XmlBoolean locked);

    /**
     * Unsets the "locked" attribute
     */
    void unsetLocked();

    /**
     * Gets the "hidden" attribute
     */
    boolean getHidden();

    /**
     * Gets (as xml) the "hidden" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetHidden();

    /**
     * True if has "hidden" attribute
     */
    boolean isSetHidden();

    /**
     * Sets the "hidden" attribute
     */
    void setHidden(boolean hidden);

    /**
     * Sets (as xml) the "hidden" attribute
     */
    void xsetHidden(org.apache.xmlbeans.XmlBoolean hidden);

    /**
     * Unsets the "hidden" attribute
     */
    void unsetHidden();

    /**
     * Gets the "count" attribute
     */
    long getCount();

    /**
     * Gets (as xml) the "count" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCount();

    /**
     * True if has "count" attribute
     */
    boolean isSetCount();

    /**
     * Sets the "count" attribute
     */
    void setCount(long count);

    /**
     * Sets (as xml) the "count" attribute
     */
    void xsetCount(org.apache.xmlbeans.XmlUnsignedInt count);

    /**
     * Unsets the "count" attribute
     */
    void unsetCount();

    /**
     * Gets the "user" attribute
     */
    java.lang.String getUser();

    /**
     * Gets (as xml) the "user" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetUser();

    /**
     * True if has "user" attribute
     */
    boolean isSetUser();

    /**
     * Sets the "user" attribute
     */
    void setUser(java.lang.String user);

    /**
     * Sets (as xml) the "user" attribute
     */
    void xsetUser(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring user);

    /**
     * Unsets the "user" attribute
     */
    void unsetUser();

    /**
     * Gets the "comment" attribute
     */
    java.lang.String getComment();

    /**
     * Gets (as xml) the "comment" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetComment();

    /**
     * True if has "comment" attribute
     */
    boolean isSetComment();

    /**
     * Sets the "comment" attribute
     */
    void setComment(java.lang.String comment);

    /**
     * Sets (as xml) the "comment" attribute
     */
    void xsetComment(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring comment);

    /**
     * Unsets the "comment" attribute
     */
    void unsetComment();
}
