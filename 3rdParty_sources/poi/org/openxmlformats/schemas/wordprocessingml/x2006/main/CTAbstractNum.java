/*
 * XML Type:  CT_AbstractNum
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AbstractNum(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAbstractNum extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctabstractnum588etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "nsid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber getNsid();

    /**
     * True if has "nsid" element
     */
    boolean isSetNsid();

    /**
     * Sets the "nsid" element
     */
    void setNsid(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber nsid);

    /**
     * Appends and returns a new empty "nsid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber addNewNsid();

    /**
     * Unsets the "nsid" element
     */
    void unsetNsid();

    /**
     * Gets the "multiLevelType" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMultiLevelType getMultiLevelType();

    /**
     * True if has "multiLevelType" element
     */
    boolean isSetMultiLevelType();

    /**
     * Sets the "multiLevelType" element
     */
    void setMultiLevelType(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMultiLevelType multiLevelType);

    /**
     * Appends and returns a new empty "multiLevelType" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMultiLevelType addNewMultiLevelType();

    /**
     * Unsets the "multiLevelType" element
     */
    void unsetMultiLevelType();

    /**
     * Gets the "tmpl" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber getTmpl();

    /**
     * True if has "tmpl" element
     */
    boolean isSetTmpl();

    /**
     * Sets the "tmpl" element
     */
    void setTmpl(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber tmpl);

    /**
     * Appends and returns a new empty "tmpl" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber addNewTmpl();

    /**
     * Unsets the "tmpl" element
     */
    void unsetTmpl();

    /**
     * Gets the "name" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getName();

    /**
     * True if has "name" element
     */
    boolean isSetName();

    /**
     * Sets the "name" element
     */
    void setName(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString name);

    /**
     * Appends and returns a new empty "name" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewName();

    /**
     * Unsets the "name" element
     */
    void unsetName();

    /**
     * Gets the "styleLink" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getStyleLink();

    /**
     * True if has "styleLink" element
     */
    boolean isSetStyleLink();

    /**
     * Sets the "styleLink" element
     */
    void setStyleLink(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString styleLink);

    /**
     * Appends and returns a new empty "styleLink" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewStyleLink();

    /**
     * Unsets the "styleLink" element
     */
    void unsetStyleLink();

    /**
     * Gets the "numStyleLink" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getNumStyleLink();

    /**
     * True if has "numStyleLink" element
     */
    boolean isSetNumStyleLink();

    /**
     * Sets the "numStyleLink" element
     */
    void setNumStyleLink(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString numStyleLink);

    /**
     * Appends and returns a new empty "numStyleLink" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewNumStyleLink();

    /**
     * Unsets the "numStyleLink" element
     */
    void unsetNumStyleLink();

    /**
     * Gets a List of "lvl" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl> getLvlList();

    /**
     * Gets array of all "lvl" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl[] getLvlArray();

    /**
     * Gets ith "lvl" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl getLvlArray(int i);

    /**
     * Returns number of "lvl" element
     */
    int sizeOfLvlArray();

    /**
     * Sets array of all "lvl" element
     */
    void setLvlArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl[] lvlArray);

    /**
     * Sets ith "lvl" element
     */
    void setLvlArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl lvl);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lvl" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl insertNewLvl(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "lvl" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl addNewLvl();

    /**
     * Removes the ith "lvl" element
     */
    void removeLvl(int i);

    /**
     * Gets the "abstractNumId" attribute
     */
    java.math.BigInteger getAbstractNumId();

    /**
     * Gets (as xml) the "abstractNumId" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetAbstractNumId();

    /**
     * Sets the "abstractNumId" attribute
     */
    void setAbstractNumId(java.math.BigInteger abstractNumId);

    /**
     * Sets (as xml) the "abstractNumId" attribute
     */
    void xsetAbstractNumId(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber abstractNumId);
}
