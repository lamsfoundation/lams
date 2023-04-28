/*
 * XML Type:  CT_WebSettings
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWebSettings
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_WebSettings(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTWebSettingsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWebSettings {
    private static final long serialVersionUID = 1L;

    public CTWebSettingsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "frameset"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "divs"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "encoding"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "optimizeForBrowser"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "relyOnVML"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "allowPNG"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotRelyOnCSS"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotSaveAsSingleFile"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotOrganizeInFolder"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotUseLongFileNames"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pixelsPerInch"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "targetScreenSz"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "saveSmartTagsAsXml"),
    };


    /**
     * Gets the "frameset" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset getFrameset() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "frameset" element
     */
    @Override
    public boolean isSetFrameset() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "frameset" element
     */
    @Override
    public void setFrameset(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset frameset) {
        generatedSetterHelperImpl(frameset, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "frameset" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset addNewFrameset() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "frameset" element
     */
    @Override
    public void unsetFrameset() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "divs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs getDivs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "divs" element
     */
    @Override
    public boolean isSetDivs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "divs" element
     */
    @Override
    public void setDivs(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs divs) {
        generatedSetterHelperImpl(divs, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "divs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs addNewDivs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "divs" element
     */
    @Override
    public void unsetDivs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "encoding" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getEncoding() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "encoding" element
     */
    @Override
    public boolean isSetEncoding() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "encoding" element
     */
    @Override
    public void setEncoding(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString encoding) {
        generatedSetterHelperImpl(encoding, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "encoding" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewEncoding() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "encoding" element
     */
    @Override
    public void unsetEncoding() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "optimizeForBrowser" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOptimizeForBrowser getOptimizeForBrowser() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOptimizeForBrowser target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOptimizeForBrowser)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "optimizeForBrowser" element
     */
    @Override
    public boolean isSetOptimizeForBrowser() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "optimizeForBrowser" element
     */
    @Override
    public void setOptimizeForBrowser(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOptimizeForBrowser optimizeForBrowser) {
        generatedSetterHelperImpl(optimizeForBrowser, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "optimizeForBrowser" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOptimizeForBrowser addNewOptimizeForBrowser() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOptimizeForBrowser target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOptimizeForBrowser)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "optimizeForBrowser" element
     */
    @Override
    public void unsetOptimizeForBrowser() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "relyOnVML" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getRelyOnVML() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "relyOnVML" element
     */
    @Override
    public boolean isSetRelyOnVML() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "relyOnVML" element
     */
    @Override
    public void setRelyOnVML(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff relyOnVML) {
        generatedSetterHelperImpl(relyOnVML, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "relyOnVML" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewRelyOnVML() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "relyOnVML" element
     */
    @Override
    public void unsetRelyOnVML() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "allowPNG" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAllowPNG() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "allowPNG" element
     */
    @Override
    public boolean isSetAllowPNG() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "allowPNG" element
     */
    @Override
    public void setAllowPNG(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff allowPNG) {
        generatedSetterHelperImpl(allowPNG, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "allowPNG" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAllowPNG() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "allowPNG" element
     */
    @Override
    public void unsetAllowPNG() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "doNotRelyOnCSS" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotRelyOnCSS() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotRelyOnCSS" element
     */
    @Override
    public boolean isSetDoNotRelyOnCSS() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "doNotRelyOnCSS" element
     */
    @Override
    public void setDoNotRelyOnCSS(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotRelyOnCSS) {
        generatedSetterHelperImpl(doNotRelyOnCSS, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotRelyOnCSS" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotRelyOnCSS() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "doNotRelyOnCSS" element
     */
    @Override
    public void unsetDoNotRelyOnCSS() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "doNotSaveAsSingleFile" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotSaveAsSingleFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotSaveAsSingleFile" element
     */
    @Override
    public boolean isSetDoNotSaveAsSingleFile() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "doNotSaveAsSingleFile" element
     */
    @Override
    public void setDoNotSaveAsSingleFile(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotSaveAsSingleFile) {
        generatedSetterHelperImpl(doNotSaveAsSingleFile, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotSaveAsSingleFile" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotSaveAsSingleFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "doNotSaveAsSingleFile" element
     */
    @Override
    public void unsetDoNotSaveAsSingleFile() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "doNotOrganizeInFolder" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotOrganizeInFolder() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotOrganizeInFolder" element
     */
    @Override
    public boolean isSetDoNotOrganizeInFolder() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "doNotOrganizeInFolder" element
     */
    @Override
    public void setDoNotOrganizeInFolder(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotOrganizeInFolder) {
        generatedSetterHelperImpl(doNotOrganizeInFolder, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotOrganizeInFolder" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotOrganizeInFolder() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "doNotOrganizeInFolder" element
     */
    @Override
    public void unsetDoNotOrganizeInFolder() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "doNotUseLongFileNames" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotUseLongFileNames() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotUseLongFileNames" element
     */
    @Override
    public boolean isSetDoNotUseLongFileNames() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "doNotUseLongFileNames" element
     */
    @Override
    public void setDoNotUseLongFileNames(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotUseLongFileNames) {
        generatedSetterHelperImpl(doNotUseLongFileNames, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotUseLongFileNames" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotUseLongFileNames() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "doNotUseLongFileNames" element
     */
    @Override
    public void unsetDoNotUseLongFileNames() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "pixelsPerInch" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getPixelsPerInch() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pixelsPerInch" element
     */
    @Override
    public boolean isSetPixelsPerInch() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "pixelsPerInch" element
     */
    @Override
    public void setPixelsPerInch(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber pixelsPerInch) {
        generatedSetterHelperImpl(pixelsPerInch, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pixelsPerInch" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewPixelsPerInch() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "pixelsPerInch" element
     */
    @Override
    public void unsetPixelsPerInch() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "targetScreenSz" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTargetScreenSz getTargetScreenSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTargetScreenSz target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTargetScreenSz)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "targetScreenSz" element
     */
    @Override
    public boolean isSetTargetScreenSz() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "targetScreenSz" element
     */
    @Override
    public void setTargetScreenSz(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTargetScreenSz targetScreenSz) {
        generatedSetterHelperImpl(targetScreenSz, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "targetScreenSz" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTargetScreenSz addNewTargetScreenSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTargetScreenSz target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTargetScreenSz)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "targetScreenSz" element
     */
    @Override
    public void unsetTargetScreenSz() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "saveSmartTagsAsXml" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSaveSmartTagsAsXml() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "saveSmartTagsAsXml" element
     */
    @Override
    public boolean isSetSaveSmartTagsAsXml() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "saveSmartTagsAsXml" element
     */
    @Override
    public void setSaveSmartTagsAsXml(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff saveSmartTagsAsXml) {
        generatedSetterHelperImpl(saveSmartTagsAsXml, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "saveSmartTagsAsXml" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSaveSmartTagsAsXml() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "saveSmartTagsAsXml" element
     */
    @Override
    public void unsetSaveSmartTagsAsXml() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }
}
