/*
 * XML Type:  CT_Frame
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Frame(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFrameImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame {
    private static final long serialVersionUID = 1L;

    public CTFrameImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sz"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "name"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "title"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "longDesc"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sourceFileName"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "marW"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "marH"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "scrollbar"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noResizeAllowed"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "linkedToFile"),
    };


    /**
     * Gets the "sz" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sz" element
     */
    @Override
    public boolean isSetSz() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "sz" element
     */
    @Override
    public void setSz(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString sz) {
        generatedSetterHelperImpl(sz, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sz" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "sz" element
     */
    @Override
    public void unsetSz() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "name" element
     */
    @Override
    public boolean isSetName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "name" element
     */
    @Override
    public void setName(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString name) {
        generatedSetterHelperImpl(name, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "name" element
     */
    @Override
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "title" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "title" element
     */
    @Override
    public boolean isSetTitle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "title" element
     */
    @Override
    public void setTitle(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString title) {
        generatedSetterHelperImpl(title, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "title" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "title" element
     */
    @Override
    public void unsetTitle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "longDesc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel getLongDesc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "longDesc" element
     */
    @Override
    public boolean isSetLongDesc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "longDesc" element
     */
    @Override
    public void setLongDesc(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel longDesc) {
        generatedSetterHelperImpl(longDesc, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "longDesc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel addNewLongDesc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "longDesc" element
     */
    @Override
    public void unsetLongDesc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "sourceFileName" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel getSourceFileName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sourceFileName" element
     */
    @Override
    public boolean isSetSourceFileName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "sourceFileName" element
     */
    @Override
    public void setSourceFileName(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel sourceFileName) {
        generatedSetterHelperImpl(sourceFileName, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sourceFileName" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel addNewSourceFileName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "sourceFileName" element
     */
    @Override
    public void unsetSourceFileName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "marW" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure getMarW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "marW" element
     */
    @Override
    public boolean isSetMarW() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "marW" element
     */
    @Override
    public void setMarW(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure marW) {
        generatedSetterHelperImpl(marW, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "marW" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure addNewMarW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "marW" element
     */
    @Override
    public void unsetMarW() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "marH" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure getMarH() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "marH" element
     */
    @Override
    public boolean isSetMarH() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "marH" element
     */
    @Override
    public void setMarH(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure marH) {
        generatedSetterHelperImpl(marH, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "marH" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure addNewMarH() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPixelsMeasure)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "marH" element
     */
    @Override
    public void unsetMarH() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "scrollbar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameScrollbar getScrollbar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameScrollbar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameScrollbar)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "scrollbar" element
     */
    @Override
    public boolean isSetScrollbar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "scrollbar" element
     */
    @Override
    public void setScrollbar(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameScrollbar scrollbar) {
        generatedSetterHelperImpl(scrollbar, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "scrollbar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameScrollbar addNewScrollbar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameScrollbar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameScrollbar)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "scrollbar" element
     */
    @Override
    public void unsetScrollbar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "noResizeAllowed" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoResizeAllowed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noResizeAllowed" element
     */
    @Override
    public boolean isSetNoResizeAllowed() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "noResizeAllowed" element
     */
    @Override
    public void setNoResizeAllowed(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noResizeAllowed) {
        generatedSetterHelperImpl(noResizeAllowed, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noResizeAllowed" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoResizeAllowed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "noResizeAllowed" element
     */
    @Override
    public void unsetNoResizeAllowed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "linkedToFile" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getLinkedToFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "linkedToFile" element
     */
    @Override
    public boolean isSetLinkedToFile() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "linkedToFile" element
     */
    @Override
    public void setLinkedToFile(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff linkedToFile) {
        generatedSetterHelperImpl(linkedToFile, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "linkedToFile" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewLinkedToFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "linkedToFile" element
     */
    @Override
    public void unsetLinkedToFile() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }
}
