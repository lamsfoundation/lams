/*
 * XML Type:  CT_NumLvl
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NumLvl(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNumLvl extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnumlvl416ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "startOverride" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getStartOverride();

    /**
     * True if has "startOverride" element
     */
    boolean isSetStartOverride();

    /**
     * Sets the "startOverride" element
     */
    void setStartOverride(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber startOverride);

    /**
     * Appends and returns a new empty "startOverride" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewStartOverride();

    /**
     * Unsets the "startOverride" element
     */
    void unsetStartOverride();

    /**
     * Gets the "lvl" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl getLvl();

    /**
     * True if has "lvl" element
     */
    boolean isSetLvl();

    /**
     * Sets the "lvl" element
     */
    void setLvl(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl lvl);

    /**
     * Appends and returns a new empty "lvl" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl addNewLvl();

    /**
     * Unsets the "lvl" element
     */
    void unsetLvl();

    /**
     * Gets the "ilvl" attribute
     */
    java.math.BigInteger getIlvl();

    /**
     * Gets (as xml) the "ilvl" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetIlvl();

    /**
     * Sets the "ilvl" attribute
     */
    void setIlvl(java.math.BigInteger ilvl);

    /**
     * Sets (as xml) the "ilvl" attribute
     */
    void xsetIlvl(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber ilvl);
}
