/*
 * XML Type:  CT_BaseStylesOverride
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_BaseStylesOverride(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTBaseStylesOverrideImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride {
    private static final long serialVersionUID = 1L;

    public CTBaseStylesOverrideImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "clrScheme"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fontScheme"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fmtScheme"),
    };


    /**
     * Gets the "clrScheme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme getClrScheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "clrScheme" element
     */
    @Override
    public boolean isSetClrScheme() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "clrScheme" element
     */
    @Override
    public void setClrScheme(org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme clrScheme) {
        generatedSetterHelperImpl(clrScheme, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clrScheme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme addNewClrScheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "clrScheme" element
     */
    @Override
    public void unsetClrScheme() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "fontScheme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme getFontScheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fontScheme" element
     */
    @Override
    public boolean isSetFontScheme() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "fontScheme" element
     */
    @Override
    public void setFontScheme(org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme fontScheme) {
        generatedSetterHelperImpl(fontScheme, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fontScheme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme addNewFontScheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "fontScheme" element
     */
    @Override
    public void unsetFontScheme() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "fmtScheme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix getFmtScheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fmtScheme" element
     */
    @Override
    public boolean isSetFmtScheme() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "fmtScheme" element
     */
    @Override
    public void setFmtScheme(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix fmtScheme) {
        generatedSetterHelperImpl(fmtScheme, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fmtScheme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix addNewFmtScheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "fmtScheme" element
     */
    @Override
    public void unsetFmtScheme() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }
}
