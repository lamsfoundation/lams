/*
 * XML Type:  CT_SlideMaster
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SlideMaster(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSlideMaster extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctslidemasterd8fctype");
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
     * Gets the "clrMap" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping getClrMap();

    /**
     * Sets the "clrMap" element
     */
    void setClrMap(org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping clrMap);

    /**
     * Appends and returns a new empty "clrMap" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping addNewClrMap();

    /**
     * Gets the "sldLayoutIdLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList getSldLayoutIdLst();

    /**
     * True if has "sldLayoutIdLst" element
     */
    boolean isSetSldLayoutIdLst();

    /**
     * Sets the "sldLayoutIdLst" element
     */
    void setSldLayoutIdLst(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList sldLayoutIdLst);

    /**
     * Appends and returns a new empty "sldLayoutIdLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList addNewSldLayoutIdLst();

    /**
     * Unsets the "sldLayoutIdLst" element
     */
    void unsetSldLayoutIdLst();

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
     * Gets the "txStyles" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles getTxStyles();

    /**
     * True if has "txStyles" element
     */
    boolean isSetTxStyles();

    /**
     * Sets the "txStyles" element
     */
    void setTxStyles(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles txStyles);

    /**
     * Appends and returns a new empty "txStyles" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles addNewTxStyles();

    /**
     * Unsets the "txStyles" element
     */
    void unsetTxStyles();

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
}
