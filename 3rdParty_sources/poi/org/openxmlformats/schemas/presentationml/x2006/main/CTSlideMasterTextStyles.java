/*
 * XML Type:  CT_SlideMasterTextStyles
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SlideMasterTextStyles(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSlideMasterTextStyles extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctslidemastertextstylesb48dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "titleStyle" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle getTitleStyle();

    /**
     * True if has "titleStyle" element
     */
    boolean isSetTitleStyle();

    /**
     * Sets the "titleStyle" element
     */
    void setTitleStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle titleStyle);

    /**
     * Appends and returns a new empty "titleStyle" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle addNewTitleStyle();

    /**
     * Unsets the "titleStyle" element
     */
    void unsetTitleStyle();

    /**
     * Gets the "bodyStyle" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle getBodyStyle();

    /**
     * True if has "bodyStyle" element
     */
    boolean isSetBodyStyle();

    /**
     * Sets the "bodyStyle" element
     */
    void setBodyStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle bodyStyle);

    /**
     * Appends and returns a new empty "bodyStyle" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle addNewBodyStyle();

    /**
     * Unsets the "bodyStyle" element
     */
    void unsetBodyStyle();

    /**
     * Gets the "otherStyle" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle getOtherStyle();

    /**
     * True if has "otherStyle" element
     */
    boolean isSetOtherStyle();

    /**
     * Sets the "otherStyle" element
     */
    void setOtherStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle otherStyle);

    /**
     * Appends and returns a new empty "otherStyle" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle addNewOtherStyle();

    /**
     * Unsets the "otherStyle" element
     */
    void unsetOtherStyle();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}
