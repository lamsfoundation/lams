/*
 * XML Type:  CT_Font
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Font(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFontImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont {
    private static final long serialVersionUID = 1L;

    public CTFontImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "altName"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "panose1"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "charset"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "family"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "notTrueType"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pitch"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sig"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "embedRegular"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "embedBold"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "embedItalic"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "embedBoldItalic"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "name"),
    };


    /**
     * Gets the "altName" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getAltName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "altName" element
     */
    @Override
    public boolean isSetAltName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "altName" element
     */
    @Override
    public void setAltName(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString altName) {
        generatedSetterHelperImpl(altName, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "altName" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewAltName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "altName" element
     */
    @Override
    public void unsetAltName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "panose1" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPanose getPanose1() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPanose target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPanose)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "panose1" element
     */
    @Override
    public boolean isSetPanose1() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "panose1" element
     */
    @Override
    public void setPanose1(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPanose panose1) {
        generatedSetterHelperImpl(panose1, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "panose1" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPanose addNewPanose1() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPanose target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPanose)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "panose1" element
     */
    @Override
    public void unsetPanose1() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "charset" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharset getCharset() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharset target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharset)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "charset" element
     */
    @Override
    public boolean isSetCharset() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "charset" element
     */
    @Override
    public void setCharset(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharset charset) {
        generatedSetterHelperImpl(charset, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "charset" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharset addNewCharset() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharset target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharset)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "charset" element
     */
    @Override
    public void unsetCharset() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "family" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontFamily getFamily() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontFamily target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontFamily)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "family" element
     */
    @Override
    public boolean isSetFamily() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "family" element
     */
    @Override
    public void setFamily(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontFamily family) {
        generatedSetterHelperImpl(family, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "family" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontFamily addNewFamily() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontFamily target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontFamily)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "family" element
     */
    @Override
    public void unsetFamily() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "notTrueType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNotTrueType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "notTrueType" element
     */
    @Override
    public boolean isSetNotTrueType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "notTrueType" element
     */
    @Override
    public void setNotTrueType(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff notTrueType) {
        generatedSetterHelperImpl(notTrueType, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "notTrueType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNotTrueType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "notTrueType" element
     */
    @Override
    public void unsetNotTrueType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "pitch" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPitch getPitch() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPitch target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPitch)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pitch" element
     */
    @Override
    public boolean isSetPitch() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "pitch" element
     */
    @Override
    public void setPitch(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPitch pitch) {
        generatedSetterHelperImpl(pitch, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pitch" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPitch addNewPitch() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPitch target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPitch)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "pitch" element
     */
    @Override
    public void unsetPitch() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "sig" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontSig getSig() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontSig target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontSig)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sig" element
     */
    @Override
    public boolean isSetSig() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "sig" element
     */
    @Override
    public void setSig(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontSig sig) {
        generatedSetterHelperImpl(sig, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sig" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontSig addNewSig() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontSig target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontSig)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "sig" element
     */
    @Override
    public void unsetSig() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "embedRegular" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel getEmbedRegular() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "embedRegular" element
     */
    @Override
    public boolean isSetEmbedRegular() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "embedRegular" element
     */
    @Override
    public void setEmbedRegular(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel embedRegular) {
        generatedSetterHelperImpl(embedRegular, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "embedRegular" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel addNewEmbedRegular() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "embedRegular" element
     */
    @Override
    public void unsetEmbedRegular() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "embedBold" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel getEmbedBold() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "embedBold" element
     */
    @Override
    public boolean isSetEmbedBold() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "embedBold" element
     */
    @Override
    public void setEmbedBold(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel embedBold) {
        generatedSetterHelperImpl(embedBold, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "embedBold" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel addNewEmbedBold() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "embedBold" element
     */
    @Override
    public void unsetEmbedBold() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "embedItalic" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel getEmbedItalic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "embedItalic" element
     */
    @Override
    public boolean isSetEmbedItalic() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "embedItalic" element
     */
    @Override
    public void setEmbedItalic(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel embedItalic) {
        generatedSetterHelperImpl(embedItalic, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "embedItalic" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel addNewEmbedItalic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "embedItalic" element
     */
    @Override
    public void unsetEmbedItalic() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "embedBoldItalic" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel getEmbedBoldItalic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "embedBoldItalic" element
     */
    @Override
    public boolean isSetEmbedBoldItalic() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "embedBoldItalic" element
     */
    @Override
    public void setEmbedBoldItalic(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel embedBoldItalic) {
        generatedSetterHelperImpl(embedBoldItalic, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "embedBoldItalic" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel addNewEmbedBoldItalic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "embedBoldItalic" element
     */
    @Override
    public void unsetEmbedBoldItalic() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "name" attribute
     */
    @Override
    public java.lang.String getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Sets the "name" attribute
     */
    @Override
    public void setName(java.lang.String name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" attribute
     */
    @Override
    public void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString name) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(name);
        }
    }
}
