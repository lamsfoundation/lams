/*
 * XML Type:  CT_TextParagraphProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TextParagraphProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTextParagraphPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties {
    private static final long serialVersionUID = 1L;

    public CTTextParagraphPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lnSpc"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "spcBef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "spcAft"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "buClrTx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "buClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "buSzTx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "buSzPct"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "buSzPts"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "buFontTx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "buFont"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "buNone"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "buAutoNum"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "buChar"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "buBlip"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tabLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "defRPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("", "marL"),
        new QName("", "marR"),
        new QName("", "lvl"),
        new QName("", "indent"),
        new QName("", "algn"),
        new QName("", "defTabSz"),
        new QName("", "rtl"),
        new QName("", "eaLnBrk"),
        new QName("", "fontAlgn"),
        new QName("", "latinLnBrk"),
        new QName("", "hangingPunct"),
    };


    /**
     * Gets the "lnSpc" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing getLnSpc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lnSpc" element
     */
    @Override
    public boolean isSetLnSpc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "lnSpc" element
     */
    @Override
    public void setLnSpc(org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing lnSpc) {
        generatedSetterHelperImpl(lnSpc, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lnSpc" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing addNewLnSpc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "lnSpc" element
     */
    @Override
    public void unsetLnSpc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "spcBef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing getSpcBef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spcBef" element
     */
    @Override
    public boolean isSetSpcBef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "spcBef" element
     */
    @Override
    public void setSpcBef(org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing spcBef) {
        generatedSetterHelperImpl(spcBef, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spcBef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing addNewSpcBef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "spcBef" element
     */
    @Override
    public void unsetSpcBef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "spcAft" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing getSpcAft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spcAft" element
     */
    @Override
    public boolean isSetSpcAft() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "spcAft" element
     */
    @Override
    public void setSpcAft(org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing spcAft) {
        generatedSetterHelperImpl(spcAft, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spcAft" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing addNewSpcAft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "spcAft" element
     */
    @Override
    public void unsetSpcAft() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "buClrTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletColorFollowText getBuClrTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletColorFollowText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletColorFollowText)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "buClrTx" element
     */
    @Override
    public boolean isSetBuClrTx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "buClrTx" element
     */
    @Override
    public void setBuClrTx(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletColorFollowText buClrTx) {
        generatedSetterHelperImpl(buClrTx, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "buClrTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletColorFollowText addNewBuClrTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletColorFollowText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletColorFollowText)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "buClrTx" element
     */
    @Override
    public void unsetBuClrTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "buClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor getBuClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "buClr" element
     */
    @Override
    public boolean isSetBuClr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "buClr" element
     */
    @Override
    public void setBuClr(org.openxmlformats.schemas.drawingml.x2006.main.CTColor buClr) {
        generatedSetterHelperImpl(buClr, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "buClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor addNewBuClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "buClr" element
     */
    @Override
    public void unsetBuClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "buSzTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizeFollowText getBuSzTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizeFollowText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizeFollowText)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "buSzTx" element
     */
    @Override
    public boolean isSetBuSzTx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "buSzTx" element
     */
    @Override
    public void setBuSzTx(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizeFollowText buSzTx) {
        generatedSetterHelperImpl(buSzTx, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "buSzTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizeFollowText addNewBuSzTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizeFollowText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizeFollowText)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "buSzTx" element
     */
    @Override
    public void unsetBuSzTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "buSzPct" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePercent getBuSzPct() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePercent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePercent)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "buSzPct" element
     */
    @Override
    public boolean isSetBuSzPct() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "buSzPct" element
     */
    @Override
    public void setBuSzPct(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePercent buSzPct) {
        generatedSetterHelperImpl(buSzPct, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "buSzPct" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePercent addNewBuSzPct() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePercent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePercent)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "buSzPct" element
     */
    @Override
    public void unsetBuSzPct() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "buSzPts" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint getBuSzPts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "buSzPts" element
     */
    @Override
    public boolean isSetBuSzPts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "buSzPts" element
     */
    @Override
    public void setBuSzPts(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint buSzPts) {
        generatedSetterHelperImpl(buSzPts, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "buSzPts" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint addNewBuSzPts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "buSzPts" element
     */
    @Override
    public void unsetBuSzPts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "buFontTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletTypefaceFollowText getBuFontTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletTypefaceFollowText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletTypefaceFollowText)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "buFontTx" element
     */
    @Override
    public boolean isSetBuFontTx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "buFontTx" element
     */
    @Override
    public void setBuFontTx(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletTypefaceFollowText buFontTx) {
        generatedSetterHelperImpl(buFontTx, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "buFontTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletTypefaceFollowText addNewBuFontTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletTypefaceFollowText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletTypefaceFollowText)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "buFontTx" element
     */
    @Override
    public void unsetBuFontTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "buFont" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getBuFont() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "buFont" element
     */
    @Override
    public boolean isSetBuFont() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "buFont" element
     */
    @Override
    public void setBuFont(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont buFont) {
        generatedSetterHelperImpl(buFont, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "buFont" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewBuFont() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "buFont" element
     */
    @Override
    public void unsetBuFont() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "buNone" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoBullet getBuNone() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoBullet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoBullet)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "buNone" element
     */
    @Override
    public boolean isSetBuNone() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "buNone" element
     */
    @Override
    public void setBuNone(org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoBullet buNone) {
        generatedSetterHelperImpl(buNone, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "buNone" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoBullet addNewBuNone() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoBullet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoBullet)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "buNone" element
     */
    @Override
    public void unsetBuNone() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "buAutoNum" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet getBuAutoNum() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "buAutoNum" element
     */
    @Override
    public boolean isSetBuAutoNum() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "buAutoNum" element
     */
    @Override
    public void setBuAutoNum(org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet buAutoNum) {
        generatedSetterHelperImpl(buAutoNum, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "buAutoNum" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet addNewBuAutoNum() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "buAutoNum" element
     */
    @Override
    public void unsetBuAutoNum() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "buChar" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharBullet getBuChar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharBullet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharBullet)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "buChar" element
     */
    @Override
    public boolean isSetBuChar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "buChar" element
     */
    @Override
    public void setBuChar(org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharBullet buChar) {
        generatedSetterHelperImpl(buChar, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "buChar" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharBullet addNewBuChar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharBullet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharBullet)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "buChar" element
     */
    @Override
    public void unsetBuChar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "buBlip" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBlipBullet getBuBlip() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBlipBullet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBlipBullet)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "buBlip" element
     */
    @Override
    public boolean isSetBuBlip() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "buBlip" element
     */
    @Override
    public void setBuBlip(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBlipBullet buBlip) {
        generatedSetterHelperImpl(buBlip, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "buBlip" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBlipBullet addNewBuBlip() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBlipBullet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBlipBullet)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "buBlip" element
     */
    @Override
    public void unsetBuBlip() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "tabLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList getTabLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tabLst" element
     */
    @Override
    public boolean isSetTabLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "tabLst" element
     */
    @Override
    public void setTabLst(org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList tabLst) {
        generatedSetterHelperImpl(tabLst, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tabLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList addNewTabLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "tabLst" element
     */
    @Override
    public void unsetTabLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "defRPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties getDefRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "defRPr" element
     */
    @Override
    public boolean isSetDefRPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "defRPr" element
     */
    @Override
    public void setDefRPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties defRPr) {
        generatedSetterHelperImpl(defRPr, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "defRPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties addNewDefRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "defRPr" element
     */
    @Override
    public void unsetDefRPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[16], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[16]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[16], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[16]);
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
            get_store().remove_element(PROPERTY_QNAME[16], 0);
        }
    }

    /**
     * Gets the "marL" attribute
     */
    @Override
    public int getMarL() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "marL" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin xgetMarL() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * True if has "marL" attribute
     */
    @Override
    public boolean isSetMarL() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[17]) != null;
        }
    }

    /**
     * Sets the "marL" attribute
     */
    @Override
    public void setMarL(int marL) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.setIntValue(marL);
        }
    }

    /**
     * Sets (as xml) the "marL" attribute
     */
    @Override
    public void xsetMarL(org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin marL) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.set(marL);
        }
    }

    /**
     * Unsets the "marL" attribute
     */
    @Override
    public void unsetMarL() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Gets the "marR" attribute
     */
    @Override
    public int getMarR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "marR" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin xgetMarR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * True if has "marR" attribute
     */
    @Override
    public boolean isSetMarR() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[18]) != null;
        }
    }

    /**
     * Sets the "marR" attribute
     */
    @Override
    public void setMarR(int marR) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.setIntValue(marR);
        }
    }

    /**
     * Sets (as xml) the "marR" attribute
     */
    @Override
    public void xsetMarR(org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin marR) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextMargin)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.set(marR);
        }
    }

    /**
     * Unsets the "marR" attribute
     */
    @Override
    public void unsetMarR() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Gets the "lvl" attribute
     */
    @Override
    public int getLvl() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "lvl" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextIndentLevelType xgetLvl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextIndentLevelType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextIndentLevelType)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * True if has "lvl" attribute
     */
    @Override
    public boolean isSetLvl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "lvl" attribute
     */
    @Override
    public void setLvl(int lvl) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setIntValue(lvl);
        }
    }

    /**
     * Sets (as xml) the "lvl" attribute
     */
    @Override
    public void xsetLvl(org.openxmlformats.schemas.drawingml.x2006.main.STTextIndentLevelType lvl) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextIndentLevelType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextIndentLevelType)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextIndentLevelType)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(lvl);
        }
    }

    /**
     * Unsets the "lvl" attribute
     */
    @Override
    public void unsetLvl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "indent" attribute
     */
    @Override
    public int getIndent() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "indent" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextIndent xgetIndent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextIndent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextIndent)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * True if has "indent" attribute
     */
    @Override
    public boolean isSetIndent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "indent" attribute
     */
    @Override
    public void setIndent(int indent) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setIntValue(indent);
        }
    }

    /**
     * Sets (as xml) the "indent" attribute
     */
    @Override
    public void xsetIndent(org.openxmlformats.schemas.drawingml.x2006.main.STTextIndent indent) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextIndent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextIndent)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextIndent)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(indent);
        }
    }

    /**
     * Unsets the "indent" attribute
     */
    @Override
    public void unsetIndent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Gets the "algn" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType.Enum getAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "algn" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType xgetAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * True if has "algn" attribute
     */
    @Override
    public boolean isSetAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[21]) != null;
        }
    }

    /**
     * Sets the "algn" attribute
     */
    @Override
    public void setAlgn(org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType.Enum algn) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.setEnumValue(algn);
        }
    }

    /**
     * Sets (as xml) the "algn" attribute
     */
    @Override
    public void xsetAlgn(org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType algn) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.set(algn);
        }
    }

    /**
     * Unsets the "algn" attribute
     */
    @Override
    public void unsetAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Gets the "defTabSz" attribute
     */
    @Override
    public java.lang.Object getDefTabSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "defTabSz" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 xgetDefTabSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * True if has "defTabSz" attribute
     */
    @Override
    public boolean isSetDefTabSz() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "defTabSz" attribute
     */
    @Override
    public void setDefTabSz(java.lang.Object defTabSz) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setObjectValue(defTabSz);
        }
    }

    /**
     * Sets (as xml) the "defTabSz" attribute
     */
    @Override
    public void xsetDefTabSz(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 defTabSz) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(defTabSz);
        }
    }

    /**
     * Unsets the "defTabSz" attribute
     */
    @Override
    public void unsetDefTabSz() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
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
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[23]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[23]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[23]);
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
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[23]);
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
            get_store().remove_attribute(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Gets the "eaLnBrk" attribute
     */
    @Override
    public boolean getEaLnBrk() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "eaLnBrk" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetEaLnBrk() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * True if has "eaLnBrk" attribute
     */
    @Override
    public boolean isSetEaLnBrk() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[24]) != null;
        }
    }

    /**
     * Sets the "eaLnBrk" attribute
     */
    @Override
    public void setEaLnBrk(boolean eaLnBrk) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.setBooleanValue(eaLnBrk);
        }
    }

    /**
     * Sets (as xml) the "eaLnBrk" attribute
     */
    @Override
    public void xsetEaLnBrk(org.apache.xmlbeans.XmlBoolean eaLnBrk) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.set(eaLnBrk);
        }
    }

    /**
     * Unsets the "eaLnBrk" attribute
     */
    @Override
    public void unsetEaLnBrk() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Gets the "fontAlgn" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType.Enum getFontAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "fontAlgn" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType xgetFontAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * True if has "fontAlgn" attribute
     */
    @Override
    public boolean isSetFontAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[25]) != null;
        }
    }

    /**
     * Sets the "fontAlgn" attribute
     */
    @Override
    public void setFontAlgn(org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType.Enum fontAlgn) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.setEnumValue(fontAlgn);
        }
    }

    /**
     * Sets (as xml) the "fontAlgn" attribute
     */
    @Override
    public void xsetFontAlgn(org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType fontAlgn) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.set(fontAlgn);
        }
    }

    /**
     * Unsets the "fontAlgn" attribute
     */
    @Override
    public void unsetFontAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Gets the "latinLnBrk" attribute
     */
    @Override
    public boolean getLatinLnBrk() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "latinLnBrk" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetLatinLnBrk() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * True if has "latinLnBrk" attribute
     */
    @Override
    public boolean isSetLatinLnBrk() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[26]) != null;
        }
    }

    /**
     * Sets the "latinLnBrk" attribute
     */
    @Override
    public void setLatinLnBrk(boolean latinLnBrk) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.setBooleanValue(latinLnBrk);
        }
    }

    /**
     * Sets (as xml) the "latinLnBrk" attribute
     */
    @Override
    public void xsetLatinLnBrk(org.apache.xmlbeans.XmlBoolean latinLnBrk) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.set(latinLnBrk);
        }
    }

    /**
     * Unsets the "latinLnBrk" attribute
     */
    @Override
    public void unsetLatinLnBrk() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Gets the "hangingPunct" attribute
     */
    @Override
    public boolean getHangingPunct() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "hangingPunct" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetHangingPunct() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * True if has "hangingPunct" attribute
     */
    @Override
    public boolean isSetHangingPunct() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[27]) != null;
        }
    }

    /**
     * Sets the "hangingPunct" attribute
     */
    @Override
    public void setHangingPunct(boolean hangingPunct) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.setBooleanValue(hangingPunct);
        }
    }

    /**
     * Sets (as xml) the "hangingPunct" attribute
     */
    @Override
    public void xsetHangingPunct(org.apache.xmlbeans.XmlBoolean hangingPunct) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.set(hangingPunct);
        }
    }

    /**
     * Unsets the "hangingPunct" attribute
     */
    @Override
    public void unsetHangingPunct() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[27]);
        }
    }
}
