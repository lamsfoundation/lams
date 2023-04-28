/*
 * XML Type:  CT_Columns
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumns
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Columns(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTColumns extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumns> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcolumns51d5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "col" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumn> getColList();

    /**
     * Gets array of all "col" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumn[] getColArray();

    /**
     * Gets ith "col" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumn getColArray(int i);

    /**
     * Returns number of "col" element
     */
    int sizeOfColArray();

    /**
     * Sets array of all "col" element
     */
    void setColArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumn[] colArray);

    /**
     * Sets ith "col" element
     */
    void setColArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumn col);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "col" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumn insertNewCol(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "col" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumn addNewCol();

    /**
     * Removes the ith "col" element
     */
    void removeCol(int i);

    /**
     * Gets the "equalWidth" attribute
     */
    java.lang.Object getEqualWidth();

    /**
     * Gets (as xml) the "equalWidth" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetEqualWidth();

    /**
     * True if has "equalWidth" attribute
     */
    boolean isSetEqualWidth();

    /**
     * Sets the "equalWidth" attribute
     */
    void setEqualWidth(java.lang.Object equalWidth);

    /**
     * Sets (as xml) the "equalWidth" attribute
     */
    void xsetEqualWidth(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff equalWidth);

    /**
     * Unsets the "equalWidth" attribute
     */
    void unsetEqualWidth();

    /**
     * Gets the "space" attribute
     */
    java.lang.Object getSpace();

    /**
     * Gets (as xml) the "space" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetSpace();

    /**
     * True if has "space" attribute
     */
    boolean isSetSpace();

    /**
     * Sets the "space" attribute
     */
    void setSpace(java.lang.Object space);

    /**
     * Sets (as xml) the "space" attribute
     */
    void xsetSpace(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure space);

    /**
     * Unsets the "space" attribute
     */
    void unsetSpace();

    /**
     * Gets the "num" attribute
     */
    java.math.BigInteger getNum();

    /**
     * Gets (as xml) the "num" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetNum();

    /**
     * True if has "num" attribute
     */
    boolean isSetNum();

    /**
     * Sets the "num" attribute
     */
    void setNum(java.math.BigInteger num);

    /**
     * Sets (as xml) the "num" attribute
     */
    void xsetNum(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber num);

    /**
     * Unsets the "num" attribute
     */
    void unsetNum();

    /**
     * Gets the "sep" attribute
     */
    java.lang.Object getSep();

    /**
     * Gets (as xml) the "sep" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetSep();

    /**
     * True if has "sep" attribute
     */
    boolean isSetSep();

    /**
     * Sets the "sep" attribute
     */
    void setSep(java.lang.Object sep);

    /**
     * Sets (as xml) the "sep" attribute
     */
    void xsetSep(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff sep);

    /**
     * Unsets the "sep" attribute
     */
    void unsetSep();
}
