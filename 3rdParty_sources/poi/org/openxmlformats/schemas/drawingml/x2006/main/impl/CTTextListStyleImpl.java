/*
 * XML Type:  CT_TextListStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TextListStyle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTextListStyleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle {
    private static final long serialVersionUID = 1L;

    public CTTextListStyleImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "defPPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lvl1pPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lvl2pPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lvl3pPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lvl4pPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lvl5pPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lvl6pPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lvl7pPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lvl8pPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lvl9pPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
    };


    /**
     * Gets the "defPPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties getDefPPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "defPPr" element
     */
    @Override
    public boolean isSetDefPPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "defPPr" element
     */
    @Override
    public void setDefPPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties defPPr) {
        generatedSetterHelperImpl(defPPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "defPPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties addNewDefPPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "defPPr" element
     */
    @Override
    public void unsetDefPPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "lvl1pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties getLvl1PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvl1pPr" element
     */
    @Override
    public boolean isSetLvl1PPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "lvl1pPr" element
     */
    @Override
    public void setLvl1PPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties lvl1PPr) {
        generatedSetterHelperImpl(lvl1PPr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvl1pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties addNewLvl1PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "lvl1pPr" element
     */
    @Override
    public void unsetLvl1PPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "lvl2pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties getLvl2PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvl2pPr" element
     */
    @Override
    public boolean isSetLvl2PPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "lvl2pPr" element
     */
    @Override
    public void setLvl2PPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties lvl2PPr) {
        generatedSetterHelperImpl(lvl2PPr, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvl2pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties addNewLvl2PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "lvl2pPr" element
     */
    @Override
    public void unsetLvl2PPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "lvl3pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties getLvl3PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvl3pPr" element
     */
    @Override
    public boolean isSetLvl3PPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "lvl3pPr" element
     */
    @Override
    public void setLvl3PPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties lvl3PPr) {
        generatedSetterHelperImpl(lvl3PPr, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvl3pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties addNewLvl3PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "lvl3pPr" element
     */
    @Override
    public void unsetLvl3PPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "lvl4pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties getLvl4PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvl4pPr" element
     */
    @Override
    public boolean isSetLvl4PPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "lvl4pPr" element
     */
    @Override
    public void setLvl4PPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties lvl4PPr) {
        generatedSetterHelperImpl(lvl4PPr, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvl4pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties addNewLvl4PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "lvl4pPr" element
     */
    @Override
    public void unsetLvl4PPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "lvl5pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties getLvl5PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvl5pPr" element
     */
    @Override
    public boolean isSetLvl5PPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "lvl5pPr" element
     */
    @Override
    public void setLvl5PPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties lvl5PPr) {
        generatedSetterHelperImpl(lvl5PPr, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvl5pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties addNewLvl5PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "lvl5pPr" element
     */
    @Override
    public void unsetLvl5PPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "lvl6pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties getLvl6PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvl6pPr" element
     */
    @Override
    public boolean isSetLvl6PPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "lvl6pPr" element
     */
    @Override
    public void setLvl6PPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties lvl6PPr) {
        generatedSetterHelperImpl(lvl6PPr, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvl6pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties addNewLvl6PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "lvl6pPr" element
     */
    @Override
    public void unsetLvl6PPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "lvl7pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties getLvl7PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvl7pPr" element
     */
    @Override
    public boolean isSetLvl7PPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "lvl7pPr" element
     */
    @Override
    public void setLvl7PPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties lvl7PPr) {
        generatedSetterHelperImpl(lvl7PPr, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvl7pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties addNewLvl7PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "lvl7pPr" element
     */
    @Override
    public void unsetLvl7PPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "lvl8pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties getLvl8PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvl8pPr" element
     */
    @Override
    public boolean isSetLvl8PPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "lvl8pPr" element
     */
    @Override
    public void setLvl8PPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties lvl8PPr) {
        generatedSetterHelperImpl(lvl8PPr, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvl8pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties addNewLvl8PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "lvl8pPr" element
     */
    @Override
    public void unsetLvl8PPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "lvl9pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties getLvl9PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvl9pPr" element
     */
    @Override
    public boolean isSetLvl9PPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "lvl9pPr" element
     */
    @Override
    public void setLvl9PPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties lvl9PPr) {
        generatedSetterHelperImpl(lvl9PPr, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvl9pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties addNewLvl9PPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "lvl9pPr" element
     */
    @Override
    public void unsetLvl9PPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[10], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[10]);
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
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }
}
