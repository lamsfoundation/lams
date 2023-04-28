/*
 * XML Type:  CT_FontScheme
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FontScheme(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFontScheme extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfontscheme232ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "majorFont" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection getMajorFont();

    /**
     * Sets the "majorFont" element
     */
    void setMajorFont(org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection majorFont);

    /**
     * Appends and returns a new empty "majorFont" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection addNewMajorFont();

    /**
     * Gets the "minorFont" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection getMinorFont();

    /**
     * Sets the "minorFont" element
     */
    void setMinorFont(org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection minorFont);

    /**
     * Appends and returns a new empty "minorFont" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection addNewMinorFont();

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

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.apache.xmlbeans.XmlString xgetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.apache.xmlbeans.XmlString name);
}
