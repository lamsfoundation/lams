/*
 * XML Type:  CT_OfficeStyleSheet
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OfficeStyleSheet(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOfficeStyleSheet extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctofficestylesheetce25type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "themeElements" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles getThemeElements();

    /**
     * Sets the "themeElements" element
     */
    void setThemeElements(org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles themeElements);

    /**
     * Appends and returns a new empty "themeElements" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles addNewThemeElements();

    /**
     * Gets the "objectDefaults" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults getObjectDefaults();

    /**
     * True if has "objectDefaults" element
     */
    boolean isSetObjectDefaults();

    /**
     * Sets the "objectDefaults" element
     */
    void setObjectDefaults(org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults objectDefaults);

    /**
     * Appends and returns a new empty "objectDefaults" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults addNewObjectDefaults();

    /**
     * Unsets the "objectDefaults" element
     */
    void unsetObjectDefaults();

    /**
     * Gets the "extraClrSchemeLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList getExtraClrSchemeLst();

    /**
     * True if has "extraClrSchemeLst" element
     */
    boolean isSetExtraClrSchemeLst();

    /**
     * Sets the "extraClrSchemeLst" element
     */
    void setExtraClrSchemeLst(org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList extraClrSchemeLst);

    /**
     * Appends and returns a new empty "extraClrSchemeLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList addNewExtraClrSchemeLst();

    /**
     * Unsets the "extraClrSchemeLst" element
     */
    void unsetExtraClrSchemeLst();

    /**
     * Gets the "custClrLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColorList getCustClrLst();

    /**
     * True if has "custClrLst" element
     */
    boolean isSetCustClrLst();

    /**
     * Sets the "custClrLst" element
     */
    void setCustClrLst(org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColorList custClrLst);

    /**
     * Appends and returns a new empty "custClrLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColorList addNewCustClrLst();

    /**
     * Unsets the "custClrLst" element
     */
    void unsetCustClrLst();

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
     * True if has "name" attribute
     */
    boolean isSetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.apache.xmlbeans.XmlString name);

    /**
     * Unsets the "name" attribute
     */
    void unsetName();
}
