/*
 * XML Type:  CT_EmbeddedFontListEntry
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_EmbeddedFontListEntry(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTEmbeddedFontListEntryImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry {
    private static final long serialVersionUID = 1L;

    public CTEmbeddedFontListEntryImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "font"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "regular"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bold"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "italic"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "boldItalic"),
    };


    /**
     * Gets the "font" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getFont() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "font" element
     */
    @Override
    public void setFont(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont font) {
        generatedSetterHelperImpl(font, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "font" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewFont() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "regular" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId getRegular() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "regular" element
     */
    @Override
    public boolean isSetRegular() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "regular" element
     */
    @Override
    public void setRegular(org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId regular) {
        generatedSetterHelperImpl(regular, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "regular" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId addNewRegular() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "regular" element
     */
    @Override
    public void unsetRegular() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "bold" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId getBold() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bold" element
     */
    @Override
    public boolean isSetBold() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "bold" element
     */
    @Override
    public void setBold(org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId bold) {
        generatedSetterHelperImpl(bold, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bold" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId addNewBold() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "bold" element
     */
    @Override
    public void unsetBold() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "italic" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId getItalic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "italic" element
     */
    @Override
    public boolean isSetItalic() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "italic" element
     */
    @Override
    public void setItalic(org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId italic) {
        generatedSetterHelperImpl(italic, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "italic" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId addNewItalic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "italic" element
     */
    @Override
    public void unsetItalic() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "boldItalic" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId getBoldItalic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "boldItalic" element
     */
    @Override
    public boolean isSetBoldItalic() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "boldItalic" element
     */
    @Override
    public void setBoldItalic(org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId boldItalic) {
        generatedSetterHelperImpl(boldItalic, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "boldItalic" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId addNewBoldItalic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "boldItalic" element
     */
    @Override
    public void unsetBoldItalic() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }
}
