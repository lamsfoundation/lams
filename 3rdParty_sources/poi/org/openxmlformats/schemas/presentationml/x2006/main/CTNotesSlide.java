/*
 * XML Type:  CT_NotesSlide
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NotesSlide(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNotesSlide extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnotesslideab75type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cSld" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData getCSld();

    /**
     * Sets the "cSld" element
     */
    void setCSld(org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData cSld);

    /**
     * Appends and returns a new empty "cSld" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData addNewCSld();

    /**
     * Gets the "clrMapOvr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride getClrMapOvr();

    /**
     * True if has "clrMapOvr" element
     */
    boolean isSetClrMapOvr();

    /**
     * Sets the "clrMapOvr" element
     */
    void setClrMapOvr(org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride clrMapOvr);

    /**
     * Appends and returns a new empty "clrMapOvr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride addNewClrMapOvr();

    /**
     * Unsets the "clrMapOvr" element
     */
    void unsetClrMapOvr();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "showMasterSp" attribute
     */
    boolean getShowMasterSp();

    /**
     * Gets (as xml) the "showMasterSp" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetShowMasterSp();

    /**
     * True if has "showMasterSp" attribute
     */
    boolean isSetShowMasterSp();

    /**
     * Sets the "showMasterSp" attribute
     */
    void setShowMasterSp(boolean showMasterSp);

    /**
     * Sets (as xml) the "showMasterSp" attribute
     */
    void xsetShowMasterSp(org.apache.xmlbeans.XmlBoolean showMasterSp);

    /**
     * Unsets the "showMasterSp" attribute
     */
    void unsetShowMasterSp();

    /**
     * Gets the "showMasterPhAnim" attribute
     */
    boolean getShowMasterPhAnim();

    /**
     * Gets (as xml) the "showMasterPhAnim" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetShowMasterPhAnim();

    /**
     * True if has "showMasterPhAnim" attribute
     */
    boolean isSetShowMasterPhAnim();

    /**
     * Sets the "showMasterPhAnim" attribute
     */
    void setShowMasterPhAnim(boolean showMasterPhAnim);

    /**
     * Sets (as xml) the "showMasterPhAnim" attribute
     */
    void xsetShowMasterPhAnim(org.apache.xmlbeans.XmlBoolean showMasterPhAnim);

    /**
     * Unsets the "showMasterPhAnim" attribute
     */
    void unsetShowMasterPhAnim();
}
