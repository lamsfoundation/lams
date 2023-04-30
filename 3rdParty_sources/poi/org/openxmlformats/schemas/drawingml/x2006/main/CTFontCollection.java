/*
 * XML Type:  CT_FontCollection
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FontCollection(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFontCollection extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfontcollectiondd68type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "latin" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getLatin();

    /**
     * Sets the "latin" element
     */
    void setLatin(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont latin);

    /**
     * Appends and returns a new empty "latin" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewLatin();

    /**
     * Gets the "ea" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getEa();

    /**
     * Sets the "ea" element
     */
    void setEa(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont ea);

    /**
     * Appends and returns a new empty "ea" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewEa();

    /**
     * Gets the "cs" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getCs();

    /**
     * Sets the "cs" element
     */
    void setCs(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont cs);

    /**
     * Appends and returns a new empty "cs" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewCs();

    /**
     * Gets a List of "font" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont> getFontList();

    /**
     * Gets array of all "font" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont[] getFontArray();

    /**
     * Gets ith "font" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont getFontArray(int i);

    /**
     * Returns number of "font" element
     */
    int sizeOfFontArray();

    /**
     * Sets array of all "font" element
     */
    void setFontArray(org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont[] fontArray);

    /**
     * Sets ith "font" element
     */
    void setFontArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont font);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "font" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont insertNewFont(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "font" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont addNewFont();

    /**
     * Removes the ith "font" element
     */
    void removeFont(int i);

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}
