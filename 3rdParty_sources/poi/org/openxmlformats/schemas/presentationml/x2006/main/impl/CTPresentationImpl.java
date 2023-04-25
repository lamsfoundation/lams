/*
 * XML Type:  CT_Presentation
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Presentation(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTPresentationImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation {
    private static final long serialVersionUID = 1L;

    public CTPresentationImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sldMasterIdLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "notesMasterIdLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "handoutMasterIdLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sldIdLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sldSz"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "notesSz"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "smartTags"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "embeddedFontLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "custShowLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "photoAlbum"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "custDataLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "kinsoku"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "defaultTextStyle"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "modifyVerifier"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
        new QName("", "serverZoom"),
        new QName("", "firstSlideNum"),
        new QName("", "showSpecialPlsOnTitleSld"),
        new QName("", "rtl"),
        new QName("", "removePersonalInfoOnSave"),
        new QName("", "compatMode"),
        new QName("", "strictFirstAndLastChars"),
        new QName("", "embedTrueTypeFonts"),
        new QName("", "saveSubsetFonts"),
        new QName("", "autoCompressPictures"),
        new QName("", "bookmarkIdSeed"),
        new QName("", "conformance"),
    };


    /**
     * Gets the "sldMasterIdLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList getSldMasterIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sldMasterIdLst" element
     */
    @Override
    public boolean isSetSldMasterIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "sldMasterIdLst" element
     */
    @Override
    public void setSldMasterIdLst(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList sldMasterIdLst) {
        generatedSetterHelperImpl(sldMasterIdLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sldMasterIdLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList addNewSldMasterIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "sldMasterIdLst" element
     */
    @Override
    public void unsetSldMasterIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "notesMasterIdLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdList getNotesMasterIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdList)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "notesMasterIdLst" element
     */
    @Override
    public boolean isSetNotesMasterIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "notesMasterIdLst" element
     */
    @Override
    public void setNotesMasterIdLst(org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdList notesMasterIdLst) {
        generatedSetterHelperImpl(notesMasterIdLst, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "notesMasterIdLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdList addNewNotesMasterIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdList)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "notesMasterIdLst" element
     */
    @Override
    public void unsetNotesMasterIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "handoutMasterIdLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdList getHandoutMasterIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdList)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "handoutMasterIdLst" element
     */
    @Override
    public boolean isSetHandoutMasterIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "handoutMasterIdLst" element
     */
    @Override
    public void setHandoutMasterIdLst(org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdList handoutMasterIdLst) {
        generatedSetterHelperImpl(handoutMasterIdLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "handoutMasterIdLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdList addNewHandoutMasterIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdList)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "handoutMasterIdLst" element
     */
    @Override
    public void unsetHandoutMasterIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "sldIdLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList getSldIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sldIdLst" element
     */
    @Override
    public boolean isSetSldIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "sldIdLst" element
     */
    @Override
    public void setSldIdLst(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList sldIdLst) {
        generatedSetterHelperImpl(sldIdLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sldIdLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList addNewSldIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "sldIdLst" element
     */
    @Override
    public void unsetSldIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "sldSz" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSize getSldSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSize target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSize)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sldSz" element
     */
    @Override
    public boolean isSetSldSz() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "sldSz" element
     */
    @Override
    public void setSldSz(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSize sldSz) {
        generatedSetterHelperImpl(sldSz, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sldSz" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSize addNewSldSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSize target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSize)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "sldSz" element
     */
    @Override
    public void unsetSldSz() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "notesSz" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D getNotesSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "notesSz" element
     */
    @Override
    public void setNotesSz(org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D notesSz) {
        generatedSetterHelperImpl(notesSz, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "notesSz" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D addNewNotesSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Gets the "smartTags" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSmartTags getSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSmartTags target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSmartTags)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "smartTags" element
     */
    @Override
    public boolean isSetSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "smartTags" element
     */
    @Override
    public void setSmartTags(org.openxmlformats.schemas.presentationml.x2006.main.CTSmartTags smartTags) {
        generatedSetterHelperImpl(smartTags, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "smartTags" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSmartTags addNewSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSmartTags target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSmartTags)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "smartTags" element
     */
    @Override
    public void unsetSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "embeddedFontLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList getEmbeddedFontLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "embeddedFontLst" element
     */
    @Override
    public boolean isSetEmbeddedFontLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "embeddedFontLst" element
     */
    @Override
    public void setEmbeddedFontLst(org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList embeddedFontLst) {
        generatedSetterHelperImpl(embeddedFontLst, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "embeddedFontLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList addNewEmbeddedFontLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "embeddedFontLst" element
     */
    @Override
    public void unsetEmbeddedFontLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "custShowLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowList getCustShowLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowList)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "custShowLst" element
     */
    @Override
    public boolean isSetCustShowLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "custShowLst" element
     */
    @Override
    public void setCustShowLst(org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowList custShowLst) {
        generatedSetterHelperImpl(custShowLst, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "custShowLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowList addNewCustShowLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowList)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "custShowLst" element
     */
    @Override
    public void unsetCustShowLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "photoAlbum" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTPhotoAlbum getPhotoAlbum() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTPhotoAlbum target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTPhotoAlbum)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "photoAlbum" element
     */
    @Override
    public boolean isSetPhotoAlbum() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "photoAlbum" element
     */
    @Override
    public void setPhotoAlbum(org.openxmlformats.schemas.presentationml.x2006.main.CTPhotoAlbum photoAlbum) {
        generatedSetterHelperImpl(photoAlbum, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "photoAlbum" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTPhotoAlbum addNewPhotoAlbum() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTPhotoAlbum target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTPhotoAlbum)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "photoAlbum" element
     */
    @Override
    public void unsetPhotoAlbum() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "custDataLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList getCustDataLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "custDataLst" element
     */
    @Override
    public boolean isSetCustDataLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "custDataLst" element
     */
    @Override
    public void setCustDataLst(org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList custDataLst) {
        generatedSetterHelperImpl(custDataLst, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "custDataLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList addNewCustDataLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "custDataLst" element
     */
    @Override
    public void unsetCustDataLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "kinsoku" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTKinsoku getKinsoku() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTKinsoku target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTKinsoku)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "kinsoku" element
     */
    @Override
    public boolean isSetKinsoku() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "kinsoku" element
     */
    @Override
    public void setKinsoku(org.openxmlformats.schemas.presentationml.x2006.main.CTKinsoku kinsoku) {
        generatedSetterHelperImpl(kinsoku, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "kinsoku" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTKinsoku addNewKinsoku() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTKinsoku target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTKinsoku)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "kinsoku" element
     */
    @Override
    public void unsetKinsoku() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "defaultTextStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle getDefaultTextStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "defaultTextStyle" element
     */
    @Override
    public boolean isSetDefaultTextStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "defaultTextStyle" element
     */
    @Override
    public void setDefaultTextStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle defaultTextStyle) {
        generatedSetterHelperImpl(defaultTextStyle, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "defaultTextStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle addNewDefaultTextStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "defaultTextStyle" element
     */
    @Override
    public void unsetDefaultTextStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "modifyVerifier" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTModifyVerifier getModifyVerifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTModifyVerifier target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTModifyVerifier)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "modifyVerifier" element
     */
    @Override
    public boolean isSetModifyVerifier() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "modifyVerifier" element
     */
    @Override
    public void setModifyVerifier(org.openxmlformats.schemas.presentationml.x2006.main.CTModifyVerifier modifyVerifier) {
        generatedSetterHelperImpl(modifyVerifier, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "modifyVerifier" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTModifyVerifier addNewModifyVerifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTModifyVerifier target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTModifyVerifier)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "modifyVerifier" element
     */
    @Override
    public void unsetModifyVerifier() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "serverZoom" attribute
     */
    @Override
    public java.lang.Object getServerZoom() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "serverZoom" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetServerZoom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return target;
        }
    }

    /**
     * True if has "serverZoom" attribute
     */
    @Override
    public boolean isSetServerZoom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[15]) != null;
        }
    }

    /**
     * Sets the "serverZoom" attribute
     */
    @Override
    public void setServerZoom(java.lang.Object serverZoom) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setObjectValue(serverZoom);
        }
    }

    /**
     * Sets (as xml) the "serverZoom" attribute
     */
    @Override
    public void xsetServerZoom(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage serverZoom) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(serverZoom);
        }
    }

    /**
     * Unsets the "serverZoom" attribute
     */
    @Override
    public void unsetServerZoom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Gets the "firstSlideNum" attribute
     */
    @Override
    public int getFirstSlideNum() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[16]);
            }
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "firstSlideNum" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetFirstSlideNum() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_default_attribute_value(PROPERTY_QNAME[16]);
            }
            return target;
        }
    }

    /**
     * True if has "firstSlideNum" attribute
     */
    @Override
    public boolean isSetFirstSlideNum() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[16]) != null;
        }
    }

    /**
     * Sets the "firstSlideNum" attribute
     */
    @Override
    public void setFirstSlideNum(int firstSlideNum) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.setIntValue(firstSlideNum);
        }
    }

    /**
     * Sets (as xml) the "firstSlideNum" attribute
     */
    @Override
    public void xsetFirstSlideNum(org.apache.xmlbeans.XmlInt firstSlideNum) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.set(firstSlideNum);
        }
    }

    /**
     * Unsets the "firstSlideNum" attribute
     */
    @Override
    public void unsetFirstSlideNum() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Gets the "showSpecialPlsOnTitleSld" attribute
     */
    @Override
    public boolean getShowSpecialPlsOnTitleSld() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[17]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showSpecialPlsOnTitleSld" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowSpecialPlsOnTitleSld() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[17]);
            }
            return target;
        }
    }

    /**
     * True if has "showSpecialPlsOnTitleSld" attribute
     */
    @Override
    public boolean isSetShowSpecialPlsOnTitleSld() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[17]) != null;
        }
    }

    /**
     * Sets the "showSpecialPlsOnTitleSld" attribute
     */
    @Override
    public void setShowSpecialPlsOnTitleSld(boolean showSpecialPlsOnTitleSld) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.setBooleanValue(showSpecialPlsOnTitleSld);
        }
    }

    /**
     * Sets (as xml) the "showSpecialPlsOnTitleSld" attribute
     */
    @Override
    public void xsetShowSpecialPlsOnTitleSld(org.apache.xmlbeans.XmlBoolean showSpecialPlsOnTitleSld) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.set(showSpecialPlsOnTitleSld);
        }
    }

    /**
     * Unsets the "showSpecialPlsOnTitleSld" attribute
     */
    @Override
    public void unsetShowSpecialPlsOnTitleSld() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Gets the "rtl" attribute
     */
    @Override
    public boolean getRtl() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[18]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "rtl" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetRtl() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[18]);
            }
            return target;
        }
    }

    /**
     * True if has "rtl" attribute
     */
    @Override
    public boolean isSetRtl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[18]) != null;
        }
    }

    /**
     * Sets the "rtl" attribute
     */
    @Override
    public void setRtl(boolean rtl) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.setBooleanValue(rtl);
        }
    }

    /**
     * Sets (as xml) the "rtl" attribute
     */
    @Override
    public void xsetRtl(org.apache.xmlbeans.XmlBoolean rtl) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.set(rtl);
        }
    }

    /**
     * Unsets the "rtl" attribute
     */
    @Override
    public void unsetRtl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Gets the "removePersonalInfoOnSave" attribute
     */
    @Override
    public boolean getRemovePersonalInfoOnSave() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[19]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "removePersonalInfoOnSave" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetRemovePersonalInfoOnSave() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[19]);
            }
            return target;
        }
    }

    /**
     * True if has "removePersonalInfoOnSave" attribute
     */
    @Override
    public boolean isSetRemovePersonalInfoOnSave() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "removePersonalInfoOnSave" attribute
     */
    @Override
    public void setRemovePersonalInfoOnSave(boolean removePersonalInfoOnSave) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setBooleanValue(removePersonalInfoOnSave);
        }
    }

    /**
     * Sets (as xml) the "removePersonalInfoOnSave" attribute
     */
    @Override
    public void xsetRemovePersonalInfoOnSave(org.apache.xmlbeans.XmlBoolean removePersonalInfoOnSave) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(removePersonalInfoOnSave);
        }
    }

    /**
     * Unsets the "removePersonalInfoOnSave" attribute
     */
    @Override
    public void unsetRemovePersonalInfoOnSave() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "compatMode" attribute
     */
    @Override
    public boolean getCompatMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[20]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "compatMode" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCompatMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[20]);
            }
            return target;
        }
    }

    /**
     * True if has "compatMode" attribute
     */
    @Override
    public boolean isSetCompatMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "compatMode" attribute
     */
    @Override
    public void setCompatMode(boolean compatMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setBooleanValue(compatMode);
        }
    }

    /**
     * Sets (as xml) the "compatMode" attribute
     */
    @Override
    public void xsetCompatMode(org.apache.xmlbeans.XmlBoolean compatMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(compatMode);
        }
    }

    /**
     * Unsets the "compatMode" attribute
     */
    @Override
    public void unsetCompatMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Gets the "strictFirstAndLastChars" attribute
     */
    @Override
    public boolean getStrictFirstAndLastChars() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[21]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "strictFirstAndLastChars" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetStrictFirstAndLastChars() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[21]);
            }
            return target;
        }
    }

    /**
     * True if has "strictFirstAndLastChars" attribute
     */
    @Override
    public boolean isSetStrictFirstAndLastChars() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[21]) != null;
        }
    }

    /**
     * Sets the "strictFirstAndLastChars" attribute
     */
    @Override
    public void setStrictFirstAndLastChars(boolean strictFirstAndLastChars) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.setBooleanValue(strictFirstAndLastChars);
        }
    }

    /**
     * Sets (as xml) the "strictFirstAndLastChars" attribute
     */
    @Override
    public void xsetStrictFirstAndLastChars(org.apache.xmlbeans.XmlBoolean strictFirstAndLastChars) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.set(strictFirstAndLastChars);
        }
    }

    /**
     * Unsets the "strictFirstAndLastChars" attribute
     */
    @Override
    public void unsetStrictFirstAndLastChars() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Gets the "embedTrueTypeFonts" attribute
     */
    @Override
    public boolean getEmbedTrueTypeFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[22]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "embedTrueTypeFonts" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetEmbedTrueTypeFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[22]);
            }
            return target;
        }
    }

    /**
     * True if has "embedTrueTypeFonts" attribute
     */
    @Override
    public boolean isSetEmbedTrueTypeFonts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "embedTrueTypeFonts" attribute
     */
    @Override
    public void setEmbedTrueTypeFonts(boolean embedTrueTypeFonts) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setBooleanValue(embedTrueTypeFonts);
        }
    }

    /**
     * Sets (as xml) the "embedTrueTypeFonts" attribute
     */
    @Override
    public void xsetEmbedTrueTypeFonts(org.apache.xmlbeans.XmlBoolean embedTrueTypeFonts) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(embedTrueTypeFonts);
        }
    }

    /**
     * Unsets the "embedTrueTypeFonts" attribute
     */
    @Override
    public void unsetEmbedTrueTypeFonts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Gets the "saveSubsetFonts" attribute
     */
    @Override
    public boolean getSaveSubsetFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[23]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "saveSubsetFonts" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSaveSubsetFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[23]);
            }
            return target;
        }
    }

    /**
     * True if has "saveSubsetFonts" attribute
     */
    @Override
    public boolean isSetSaveSubsetFonts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[23]) != null;
        }
    }

    /**
     * Sets the "saveSubsetFonts" attribute
     */
    @Override
    public void setSaveSubsetFonts(boolean saveSubsetFonts) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.setBooleanValue(saveSubsetFonts);
        }
    }

    /**
     * Sets (as xml) the "saveSubsetFonts" attribute
     */
    @Override
    public void xsetSaveSubsetFonts(org.apache.xmlbeans.XmlBoolean saveSubsetFonts) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.set(saveSubsetFonts);
        }
    }

    /**
     * Unsets the "saveSubsetFonts" attribute
     */
    @Override
    public void unsetSaveSubsetFonts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Gets the "autoCompressPictures" attribute
     */
    @Override
    public boolean getAutoCompressPictures() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[24]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "autoCompressPictures" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAutoCompressPictures() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[24]);
            }
            return target;
        }
    }

    /**
     * True if has "autoCompressPictures" attribute
     */
    @Override
    public boolean isSetAutoCompressPictures() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[24]) != null;
        }
    }

    /**
     * Sets the "autoCompressPictures" attribute
     */
    @Override
    public void setAutoCompressPictures(boolean autoCompressPictures) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.setBooleanValue(autoCompressPictures);
        }
    }

    /**
     * Sets (as xml) the "autoCompressPictures" attribute
     */
    @Override
    public void xsetAutoCompressPictures(org.apache.xmlbeans.XmlBoolean autoCompressPictures) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.set(autoCompressPictures);
        }
    }

    /**
     * Unsets the "autoCompressPictures" attribute
     */
    @Override
    public void unsetAutoCompressPictures() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Gets the "bookmarkIdSeed" attribute
     */
    @Override
    public long getBookmarkIdSeed() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[25]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "bookmarkIdSeed" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STBookmarkIdSeed xgetBookmarkIdSeed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STBookmarkIdSeed target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STBookmarkIdSeed)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STBookmarkIdSeed)get_default_attribute_value(PROPERTY_QNAME[25]);
            }
            return target;
        }
    }

    /**
     * True if has "bookmarkIdSeed" attribute
     */
    @Override
    public boolean isSetBookmarkIdSeed() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[25]) != null;
        }
    }

    /**
     * Sets the "bookmarkIdSeed" attribute
     */
    @Override
    public void setBookmarkIdSeed(long bookmarkIdSeed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.setLongValue(bookmarkIdSeed);
        }
    }

    /**
     * Sets (as xml) the "bookmarkIdSeed" attribute
     */
    @Override
    public void xsetBookmarkIdSeed(org.openxmlformats.schemas.presentationml.x2006.main.STBookmarkIdSeed bookmarkIdSeed) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STBookmarkIdSeed target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STBookmarkIdSeed)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STBookmarkIdSeed)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.set(bookmarkIdSeed);
        }
    }

    /**
     * Unsets the "bookmarkIdSeed" attribute
     */
    @Override
    public void unsetBookmarkIdSeed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Gets the "conformance" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass.Enum getConformance() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return (target == null) ? null : (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "conformance" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass xgetConformance() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * True if has "conformance" attribute
     */
    @Override
    public boolean isSetConformance() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[26]) != null;
        }
    }

    /**
     * Sets the "conformance" attribute
     */
    @Override
    public void setConformance(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass.Enum conformance) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.setEnumValue(conformance);
        }
    }

    /**
     * Sets (as xml) the "conformance" attribute
     */
    @Override
    public void xsetConformance(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass conformance) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.set(conformance);
        }
    }

    /**
     * Unsets the "conformance" attribute
     */
    @Override
    public void unsetConformance() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[26]);
        }
    }
}
