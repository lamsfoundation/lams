/*
 * XML Type:  CT_NumPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NumPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNumPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnumpr16aatype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ilvl" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getIlvl();

    /**
     * True if has "ilvl" element
     */
    boolean isSetIlvl();

    /**
     * Sets the "ilvl" element
     */
    void setIlvl(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber ilvl);

    /**
     * Appends and returns a new empty "ilvl" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewIlvl();

    /**
     * Unsets the "ilvl" element
     */
    void unsetIlvl();

    /**
     * Gets the "numId" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getNumId();

    /**
     * True if has "numId" element
     */
    boolean isSetNumId();

    /**
     * Sets the "numId" element
     */
    void setNumId(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber numId);

    /**
     * Appends and returns a new empty "numId" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewNumId();

    /**
     * Unsets the "numId" element
     */
    void unsetNumId();

    /**
     * Gets the "numberingChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering getNumberingChange();

    /**
     * True if has "numberingChange" element
     */
    boolean isSetNumberingChange();

    /**
     * Sets the "numberingChange" element
     */
    void setNumberingChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering numberingChange);

    /**
     * Appends and returns a new empty "numberingChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering addNewNumberingChange();

    /**
     * Unsets the "numberingChange" element
     */
    void unsetNumberingChange();

    /**
     * Gets the "ins" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getIns();

    /**
     * True if has "ins" element
     */
    boolean isSetIns();

    /**
     * Sets the "ins" element
     */
    void setIns(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange ins);

    /**
     * Appends and returns a new empty "ins" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewIns();

    /**
     * Unsets the "ins" element
     */
    void unsetIns();
}
