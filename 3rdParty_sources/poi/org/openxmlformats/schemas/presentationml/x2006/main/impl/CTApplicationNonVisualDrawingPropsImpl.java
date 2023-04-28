/*
 * XML Type:  CT_ApplicationNonVisualDrawingProps
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ApplicationNonVisualDrawingProps(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTApplicationNonVisualDrawingPropsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps {
    private static final long serialVersionUID = 1L;

    public CTApplicationNonVisualDrawingPropsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "ph"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "audioCd"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "wavAudioFile"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "audioFile"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "videoFile"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "quickTimeFile"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "custDataLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
        new QName("", "isPhoto"),
        new QName("", "userDrawn"),
    };


    /**
     * Gets the "ph" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder getPh() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ph" element
     */
    @Override
    public boolean isSetPh() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "ph" element
     */
    @Override
    public void setPh(org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder ph) {
        generatedSetterHelperImpl(ph, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ph" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder addNewPh() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "ph" element
     */
    @Override
    public void unsetPh() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "audioCd" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCD getAudioCd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCD target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCD)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "audioCd" element
     */
    @Override
    public boolean isSetAudioCd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "audioCd" element
     */
    @Override
    public void setAudioCd(org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCD audioCd) {
        generatedSetterHelperImpl(audioCd, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "audioCd" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCD addNewAudioCd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCD target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCD)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "audioCd" element
     */
    @Override
    public void unsetAudioCd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "wavAudioFile" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile getWavAudioFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "wavAudioFile" element
     */
    @Override
    public boolean isSetWavAudioFile() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "wavAudioFile" element
     */
    @Override
    public void setWavAudioFile(org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile wavAudioFile) {
        generatedSetterHelperImpl(wavAudioFile, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wavAudioFile" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile addNewWavAudioFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "wavAudioFile" element
     */
    @Override
    public void unsetWavAudioFile() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "audioFile" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAudioFile getAudioFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAudioFile target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAudioFile)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "audioFile" element
     */
    @Override
    public boolean isSetAudioFile() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "audioFile" element
     */
    @Override
    public void setAudioFile(org.openxmlformats.schemas.drawingml.x2006.main.CTAudioFile audioFile) {
        generatedSetterHelperImpl(audioFile, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "audioFile" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAudioFile addNewAudioFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAudioFile target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAudioFile)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "audioFile" element
     */
    @Override
    public void unsetAudioFile() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "videoFile" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile getVideoFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "videoFile" element
     */
    @Override
    public boolean isSetVideoFile() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "videoFile" element
     */
    @Override
    public void setVideoFile(org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile videoFile) {
        generatedSetterHelperImpl(videoFile, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "videoFile" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile addNewVideoFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "videoFile" element
     */
    @Override
    public void unsetVideoFile() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "quickTimeFile" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTQuickTimeFile getQuickTimeFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTQuickTimeFile target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTQuickTimeFile)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "quickTimeFile" element
     */
    @Override
    public boolean isSetQuickTimeFile() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "quickTimeFile" element
     */
    @Override
    public void setQuickTimeFile(org.openxmlformats.schemas.drawingml.x2006.main.CTQuickTimeFile quickTimeFile) {
        generatedSetterHelperImpl(quickTimeFile, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "quickTimeFile" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTQuickTimeFile addNewQuickTimeFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTQuickTimeFile target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTQuickTimeFile)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "quickTimeFile" element
     */
    @Override
    public void unsetQuickTimeFile() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
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
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList)get_store().find_element_user(PROPERTY_QNAME[6], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "custDataLst" element
     */
    @Override
    public void setCustDataLst(org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList custDataLst) {
        generatedSetterHelperImpl(custDataLst, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "custDataLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList addNewCustDataLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList)get_store().add_element_user(PROPERTY_QNAME[6]);
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
            get_store().remove_element(PROPERTY_QNAME[6], 0);
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
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[7], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[7]);
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
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "isPhoto" attribute
     */
    @Override
    public boolean getIsPhoto() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "isPhoto" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetIsPhoto() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return target;
        }
    }

    /**
     * True if has "isPhoto" attribute
     */
    @Override
    public boolean isSetIsPhoto() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "isPhoto" attribute
     */
    @Override
    public void setIsPhoto(boolean isPhoto) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setBooleanValue(isPhoto);
        }
    }

    /**
     * Sets (as xml) the "isPhoto" attribute
     */
    @Override
    public void xsetIsPhoto(org.apache.xmlbeans.XmlBoolean isPhoto) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(isPhoto);
        }
    }

    /**
     * Unsets the "isPhoto" attribute
     */
    @Override
    public void unsetIsPhoto() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "userDrawn" attribute
     */
    @Override
    public boolean getUserDrawn() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "userDrawn" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetUserDrawn() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return target;
        }
    }

    /**
     * True if has "userDrawn" attribute
     */
    @Override
    public boolean isSetUserDrawn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "userDrawn" attribute
     */
    @Override
    public void setUserDrawn(boolean userDrawn) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setBooleanValue(userDrawn);
        }
    }

    /**
     * Sets (as xml) the "userDrawn" attribute
     */
    @Override
    public void xsetUserDrawn(org.apache.xmlbeans.XmlBoolean userDrawn) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(userDrawn);
        }
    }

    /**
     * Unsets the "userDrawn" attribute
     */
    @Override
    public void unsetUserDrawn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }
}
