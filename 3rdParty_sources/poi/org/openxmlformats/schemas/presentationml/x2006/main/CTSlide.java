/*
 * XML Type:  CT_Slide
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlide
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Slide(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSlide extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTSlide> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctslided7betype");
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
     * Gets the "transition" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition getTransition();

    /**
     * True if has "transition" element
     */
    boolean isSetTransition();

    /**
     * Sets the "transition" element
     */
    void setTransition(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition transition);

    /**
     * Appends and returns a new empty "transition" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition addNewTransition();

    /**
     * Unsets the "transition" element
     */
    void unsetTransition();

    /**
     * Gets the "timing" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming getTiming();

    /**
     * True if has "timing" element
     */
    boolean isSetTiming();

    /**
     * Sets the "timing" element
     */
    void setTiming(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming timing);

    /**
     * Appends and returns a new empty "timing" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming addNewTiming();

    /**
     * Unsets the "timing" element
     */
    void unsetTiming();

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

    /**
     * Gets the "show" attribute
     */
    boolean getShow();

    /**
     * Gets (as xml) the "show" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetShow();

    /**
     * True if has "show" attribute
     */
    boolean isSetShow();

    /**
     * Sets the "show" attribute
     */
    void setShow(boolean show);

    /**
     * Sets (as xml) the "show" attribute
     */
    void xsetShow(org.apache.xmlbeans.XmlBoolean show);

    /**
     * Unsets the "show" attribute
     */
    void unsetShow();
}
