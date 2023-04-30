/*
 * XML Type:  CT_Sst
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Sst(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSst extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsst44f3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "si" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst> getSiList();

    /**
     * Gets array of all "si" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst[] getSiArray();

    /**
     * Gets ith "si" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst getSiArray(int i);

    /**
     * Returns number of "si" element
     */
    int sizeOfSiArray();

    /**
     * Sets array of all "si" element
     */
    void setSiArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst[] siArray);

    /**
     * Sets ith "si" element
     */
    void setSiArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst si);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "si" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst insertNewSi(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "si" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst addNewSi();

    /**
     * Removes the ith "si" element
     */
    void removeSi(int i);

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
     * Gets the "uniqueCount" attribute
     */
    long getUniqueCount();

    /**
     * Gets (as xml) the "uniqueCount" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetUniqueCount();

    /**
     * True if has "uniqueCount" attribute
     */
    boolean isSetUniqueCount();

    /**
     * Sets the "uniqueCount" attribute
     */
    void setUniqueCount(long uniqueCount);

    /**
     * Sets (as xml) the "uniqueCount" attribute
     */
    void xsetUniqueCount(org.apache.xmlbeans.XmlUnsignedInt uniqueCount);

    /**
     * Unsets the "uniqueCount" attribute
     */
    void unsetUniqueCount();
}
