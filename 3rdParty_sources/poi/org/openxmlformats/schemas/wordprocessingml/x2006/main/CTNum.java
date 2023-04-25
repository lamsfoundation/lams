/*
 * XML Type:  CT_Num
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Num(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNum extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnume94ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "abstractNumId" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getAbstractNumId();

    /**
     * Sets the "abstractNumId" element
     */
    void setAbstractNumId(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber abstractNumId);

    /**
     * Appends and returns a new empty "abstractNumId" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewAbstractNumId();

    /**
     * Gets a List of "lvlOverride" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl> getLvlOverrideList();

    /**
     * Gets array of all "lvlOverride" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl[] getLvlOverrideArray();

    /**
     * Gets ith "lvlOverride" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl getLvlOverrideArray(int i);

    /**
     * Returns number of "lvlOverride" element
     */
    int sizeOfLvlOverrideArray();

    /**
     * Sets array of all "lvlOverride" element
     */
    void setLvlOverrideArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl[] lvlOverrideArray);

    /**
     * Sets ith "lvlOverride" element
     */
    void setLvlOverrideArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl lvlOverride);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lvlOverride" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl insertNewLvlOverride(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "lvlOverride" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl addNewLvlOverride();

    /**
     * Removes the ith "lvlOverride" element
     */
    void removeLvlOverride(int i);

    /**
     * Gets the "numId" attribute
     */
    java.math.BigInteger getNumId();

    /**
     * Gets (as xml) the "numId" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetNumId();

    /**
     * Sets the "numId" attribute
     */
    void setNumId(java.math.BigInteger numId);

    /**
     * Sets (as xml) the "numId" attribute
     */
    void xsetNumId(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber numId);
}
