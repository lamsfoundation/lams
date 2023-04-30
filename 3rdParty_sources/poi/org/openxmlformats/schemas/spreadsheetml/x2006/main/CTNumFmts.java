/*
 * XML Type:  CT_NumFmts
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NumFmts(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNumFmts extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnumfmtsb58btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "numFmt" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt> getNumFmtList();

    /**
     * Gets array of all "numFmt" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt[] getNumFmtArray();

    /**
     * Gets ith "numFmt" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt getNumFmtArray(int i);

    /**
     * Returns number of "numFmt" element
     */
    int sizeOfNumFmtArray();

    /**
     * Sets array of all "numFmt" element
     */
    void setNumFmtArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt[] numFmtArray);

    /**
     * Sets ith "numFmt" element
     */
    void setNumFmtArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt numFmt);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "numFmt" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt insertNewNumFmt(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "numFmt" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt addNewNumFmt();

    /**
     * Removes the ith "numFmt" element
     */
    void removeNumFmt(int i);

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
}
