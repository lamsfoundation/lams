/*
 * XML Type:  CT_FramesetSplitbar
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FramesetSplitbar(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFramesetSplitbarImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar {
    private static final long serialVersionUID = 1L;

    public CTFramesetSplitbarImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "w"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "color"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noBorder"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "flatBorders"),
    };


    /**
     * Gets the "w" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure getW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "w" element
     */
    @Override
    public boolean isSetW() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "w" element
     */
    @Override
    public void setW(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure w) {
        generatedSetterHelperImpl(w, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "w" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure addNewW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "w" element
     */
    @Override
    public void unsetW() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "color" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor getColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "color" element
     */
    @Override
    public boolean isSetColor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "color" element
     */
    @Override
    public void setColor(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor color) {
        generatedSetterHelperImpl(color, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "color" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor addNewColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "color" element
     */
    @Override
    public void unsetColor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "noBorder" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoBorder() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noBorder" element
     */
    @Override
    public boolean isSetNoBorder() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "noBorder" element
     */
    @Override
    public void setNoBorder(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noBorder) {
        generatedSetterHelperImpl(noBorder, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noBorder" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoBorder() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "noBorder" element
     */
    @Override
    public void unsetNoBorder() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "flatBorders" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getFlatBorders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "flatBorders" element
     */
    @Override
    public boolean isSetFlatBorders() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "flatBorders" element
     */
    @Override
    public void setFlatBorders(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff flatBorders) {
        generatedSetterHelperImpl(flatBorders, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "flatBorders" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewFlatBorders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "flatBorders" element
     */
    @Override
    public void unsetFlatBorders() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}
