/*
 * XML Type:  CT_SlideLayout
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SlideLayout(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSlideLayout extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctslidelayouteb34type");
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
     * Gets the "hf" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter getHf();

    /**
     * True if has "hf" element
     */
    boolean isSetHf();

    /**
     * Sets the "hf" element
     */
    void setHf(org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter hf);

    /**
     * Appends and returns a new empty "hf" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter addNewHf();

    /**
     * Unsets the "hf" element
     */
    void unsetHf();

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
     * Gets the "matchingName" attribute
     */
    java.lang.String getMatchingName();

    /**
     * Gets (as xml) the "matchingName" attribute
     */
    org.apache.xmlbeans.XmlString xgetMatchingName();

    /**
     * True if has "matchingName" attribute
     */
    boolean isSetMatchingName();

    /**
     * Sets the "matchingName" attribute
     */
    void setMatchingName(java.lang.String matchingName);

    /**
     * Sets (as xml) the "matchingName" attribute
     */
    void xsetMatchingName(org.apache.xmlbeans.XmlString matchingName);

    /**
     * Unsets the "matchingName" attribute
     */
    void unsetMatchingName();

    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STSlideLayoutType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STSlideLayoutType xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.presentationml.x2006.main.STSlideLayoutType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.presentationml.x2006.main.STSlideLayoutType type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();

    /**
     * Gets the "preserve" attribute
     */
    boolean getPreserve();

    /**
     * Gets (as xml) the "preserve" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetPreserve();

    /**
     * True if has "preserve" attribute
     */
    boolean isSetPreserve();

    /**
     * Sets the "preserve" attribute
     */
    void setPreserve(boolean preserve);

    /**
     * Sets (as xml) the "preserve" attribute
     */
    void xsetPreserve(org.apache.xmlbeans.XmlBoolean preserve);

    /**
     * Unsets the "preserve" attribute
     */
    void unsetPreserve();

    /**
     * Gets the "userDrawn" attribute
     */
    boolean getUserDrawn();

    /**
     * Gets (as xml) the "userDrawn" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUserDrawn();

    /**
     * True if has "userDrawn" attribute
     */
    boolean isSetUserDrawn();

    /**
     * Sets the "userDrawn" attribute
     */
    void setUserDrawn(boolean userDrawn);

    /**
     * Sets (as xml) the "userDrawn" attribute
     */
    void xsetUserDrawn(org.apache.xmlbeans.XmlBoolean userDrawn);

    /**
     * Unsets the "userDrawn" attribute
     */
    void unsetUserDrawn();
}
