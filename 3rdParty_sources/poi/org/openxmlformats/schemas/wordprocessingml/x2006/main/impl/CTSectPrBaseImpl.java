/*
 * XML Type:  CT_SectPrBase
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrBase
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SectPrBase(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTSectPrBaseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrBase {
    private static final long serialVersionUID = 1L;

    public CTSectPrBaseImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "footnotePr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "endnotePr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "type"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pgSz"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pgMar"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "paperSrc"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pgBorders"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lnNumType"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pgNumType"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "cols"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "formProt"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "vAlign"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noEndnote"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "titlePg"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "textDirection"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bidi"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rtlGutter"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "docGrid"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "printerSettings"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsidRPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsidDel"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsidR"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsidSect"),
    };


    /**
     * Gets the "footnotePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnProps getFootnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnProps target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnProps)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "footnotePr" element
     */
    @Override
    public boolean isSetFootnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "footnotePr" element
     */
    @Override
    public void setFootnotePr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnProps footnotePr) {
        generatedSetterHelperImpl(footnotePr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "footnotePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnProps addNewFootnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnProps target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnProps)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "footnotePr" element
     */
    @Override
    public void unsetFootnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "endnotePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnProps getEndnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnProps target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnProps)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "endnotePr" element
     */
    @Override
    public boolean isSetEndnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "endnotePr" element
     */
    @Override
    public void setEndnotePr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnProps endnotePr) {
        generatedSetterHelperImpl(endnotePr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "endnotePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnProps addNewEndnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnProps target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnProps)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "endnotePr" element
     */
    @Override
    public void unsetEndnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "type" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectType getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectType)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "type" element
     */
    @Override
    public boolean isSetType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "type" element
     */
    @Override
    public void setType(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectType type) {
        generatedSetterHelperImpl(type, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "type" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectType addNewType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "type" element
     */
    @Override
    public void unsetType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "pgSz" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz getPgSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pgSz" element
     */
    @Override
    public boolean isSetPgSz() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "pgSz" element
     */
    @Override
    public void setPgSz(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz pgSz) {
        generatedSetterHelperImpl(pgSz, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pgSz" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz addNewPgSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "pgSz" element
     */
    @Override
    public void unsetPgSz() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "pgMar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar getPgMar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pgMar" element
     */
    @Override
    public boolean isSetPgMar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "pgMar" element
     */
    @Override
    public void setPgMar(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar pgMar) {
        generatedSetterHelperImpl(pgMar, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pgMar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar addNewPgMar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "pgMar" element
     */
    @Override
    public void unsetPgMar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "paperSrc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPaperSource getPaperSrc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPaperSource target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPaperSource)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "paperSrc" element
     */
    @Override
    public boolean isSetPaperSrc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "paperSrc" element
     */
    @Override
    public void setPaperSrc(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPaperSource paperSrc) {
        generatedSetterHelperImpl(paperSrc, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "paperSrc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPaperSource addNewPaperSrc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPaperSource target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPaperSource)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "paperSrc" element
     */
    @Override
    public void unsetPaperSrc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "pgBorders" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorders getPgBorders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorders target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorders)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pgBorders" element
     */
    @Override
    public boolean isSetPgBorders() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "pgBorders" element
     */
    @Override
    public void setPgBorders(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorders pgBorders) {
        generatedSetterHelperImpl(pgBorders, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pgBorders" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorders addNewPgBorders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorders target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorders)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "pgBorders" element
     */
    @Override
    public void unsetPgBorders() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "lnNumType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLineNumber getLnNumType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLineNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLineNumber)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lnNumType" element
     */
    @Override
    public boolean isSetLnNumType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "lnNumType" element
     */
    @Override
    public void setLnNumType(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLineNumber lnNumType) {
        generatedSetterHelperImpl(lnNumType, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lnNumType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLineNumber addNewLnNumType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLineNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLineNumber)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "lnNumType" element
     */
    @Override
    public void unsetLnNumType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "pgNumType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageNumber getPgNumType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageNumber)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pgNumType" element
     */
    @Override
    public boolean isSetPgNumType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "pgNumType" element
     */
    @Override
    public void setPgNumType(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageNumber pgNumType) {
        generatedSetterHelperImpl(pgNumType, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pgNumType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageNumber addNewPgNumType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageNumber)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "pgNumType" element
     */
    @Override
    public void unsetPgNumType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "cols" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumns getCols() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumns target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumns)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cols" element
     */
    @Override
    public boolean isSetCols() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "cols" element
     */
    @Override
    public void setCols(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumns cols) {
        generatedSetterHelperImpl(cols, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cols" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumns addNewCols() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumns target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColumns)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "cols" element
     */
    @Override
    public void unsetCols() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "formProt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getFormProt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "formProt" element
     */
    @Override
    public boolean isSetFormProt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "formProt" element
     */
    @Override
    public void setFormProt(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff formProt) {
        generatedSetterHelperImpl(formProt, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "formProt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewFormProt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "formProt" element
     */
    @Override
    public void unsetFormProt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "vAlign" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc getVAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "vAlign" element
     */
    @Override
    public boolean isSetVAlign() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "vAlign" element
     */
    @Override
    public void setVAlign(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc vAlign) {
        generatedSetterHelperImpl(vAlign, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "vAlign" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc addNewVAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "vAlign" element
     */
    @Override
    public void unsetVAlign() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "noEndnote" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoEndnote() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noEndnote" element
     */
    @Override
    public boolean isSetNoEndnote() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "noEndnote" element
     */
    @Override
    public void setNoEndnote(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noEndnote) {
        generatedSetterHelperImpl(noEndnote, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noEndnote" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoEndnote() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "noEndnote" element
     */
    @Override
    public void unsetNoEndnote() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "titlePg" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getTitlePg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "titlePg" element
     */
    @Override
    public boolean isSetTitlePg() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "titlePg" element
     */
    @Override
    public void setTitlePg(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff titlePg) {
        generatedSetterHelperImpl(titlePg, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "titlePg" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewTitlePg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "titlePg" element
     */
    @Override
    public void unsetTitlePg() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "textDirection" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection getTextDirection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "textDirection" element
     */
    @Override
    public boolean isSetTextDirection() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "textDirection" element
     */
    @Override
    public void setTextDirection(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection textDirection) {
        generatedSetterHelperImpl(textDirection, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "textDirection" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection addNewTextDirection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "textDirection" element
     */
    @Override
    public void unsetTextDirection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "bidi" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBidi() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bidi" element
     */
    @Override
    public boolean isSetBidi() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "bidi" element
     */
    @Override
    public void setBidi(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff bidi) {
        generatedSetterHelperImpl(bidi, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bidi" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewBidi() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "bidi" element
     */
    @Override
    public void unsetBidi() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }

    /**
     * Gets the "rtlGutter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getRtlGutter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[16], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rtlGutter" element
     */
    @Override
    public boolean isSetRtlGutter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]) != 0;
        }
    }

    /**
     * Sets the "rtlGutter" element
     */
    @Override
    public void setRtlGutter(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff rtlGutter) {
        generatedSetterHelperImpl(rtlGutter, PROPERTY_QNAME[16], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rtlGutter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewRtlGutter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Unsets the "rtlGutter" element
     */
    @Override
    public void unsetRtlGutter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], 0);
        }
    }

    /**
     * Gets the "docGrid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocGrid getDocGrid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocGrid target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocGrid)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "docGrid" element
     */
    @Override
    public boolean isSetDocGrid() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]) != 0;
        }
    }

    /**
     * Sets the "docGrid" element
     */
    @Override
    public void setDocGrid(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocGrid docGrid) {
        generatedSetterHelperImpl(docGrid, PROPERTY_QNAME[17], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "docGrid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocGrid addNewDocGrid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocGrid target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocGrid)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Unsets the "docGrid" element
     */
    @Override
    public void unsetDocGrid() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], 0);
        }
    }

    /**
     * Gets the "printerSettings" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel getPrinterSettings() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "printerSettings" element
     */
    @Override
    public boolean isSetPrinterSettings() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]) != 0;
        }
    }

    /**
     * Sets the "printerSettings" element
     */
    @Override
    public void setPrinterSettings(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel printerSettings) {
        generatedSetterHelperImpl(printerSettings, PROPERTY_QNAME[18], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "printerSettings" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel addNewPrinterSettings() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Unsets the "printerSettings" element
     */
    @Override
    public void unsetPrinterSettings() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], 0);
        }
    }

    /**
     * Gets the "rsidRPr" attribute
     */
    @Override
    public byte[] getRsidRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "rsidRPr" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetRsidRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * True if has "rsidRPr" attribute
     */
    @Override
    public boolean isSetRsidRPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "rsidRPr" attribute
     */
    @Override
    public void setRsidRPr(byte[] rsidRPr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setByteArrayValue(rsidRPr);
        }
    }

    /**
     * Sets (as xml) the "rsidRPr" attribute
     */
    @Override
    public void xsetRsidRPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber rsidRPr) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(rsidRPr);
        }
    }

    /**
     * Unsets the "rsidRPr" attribute
     */
    @Override
    public void unsetRsidRPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "rsidDel" attribute
     */
    @Override
    public byte[] getRsidDel() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "rsidDel" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetRsidDel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * True if has "rsidDel" attribute
     */
    @Override
    public boolean isSetRsidDel() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "rsidDel" attribute
     */
    @Override
    public void setRsidDel(byte[] rsidDel) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setByteArrayValue(rsidDel);
        }
    }

    /**
     * Sets (as xml) the "rsidDel" attribute
     */
    @Override
    public void xsetRsidDel(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber rsidDel) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(rsidDel);
        }
    }

    /**
     * Unsets the "rsidDel" attribute
     */
    @Override
    public void unsetRsidDel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Gets the "rsidR" attribute
     */
    @Override
    public byte[] getRsidR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "rsidR" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetRsidR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * True if has "rsidR" attribute
     */
    @Override
    public boolean isSetRsidR() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[21]) != null;
        }
    }

    /**
     * Sets the "rsidR" attribute
     */
    @Override
    public void setRsidR(byte[] rsidR) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.setByteArrayValue(rsidR);
        }
    }

    /**
     * Sets (as xml) the "rsidR" attribute
     */
    @Override
    public void xsetRsidR(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber rsidR) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.set(rsidR);
        }
    }

    /**
     * Unsets the "rsidR" attribute
     */
    @Override
    public void unsetRsidR() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Gets the "rsidSect" attribute
     */
    @Override
    public byte[] getRsidSect() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "rsidSect" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetRsidSect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * True if has "rsidSect" attribute
     */
    @Override
    public boolean isSetRsidSect() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "rsidSect" attribute
     */
    @Override
    public void setRsidSect(byte[] rsidSect) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setByteArrayValue(rsidSect);
        }
    }

    /**
     * Sets (as xml) the "rsidSect" attribute
     */
    @Override
    public void xsetRsidSect(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber rsidSect) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(rsidSect);
        }
    }

    /**
     * Unsets the "rsidSect" attribute
     */
    @Override
    public void unsetRsidSect() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
        }
    }
}
