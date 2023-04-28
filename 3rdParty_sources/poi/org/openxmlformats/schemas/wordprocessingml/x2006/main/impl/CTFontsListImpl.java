/*
 * XML Type:  CT_FontsList
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FontsList(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFontsListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList {
    private static final long serialVersionUID = 1L;

    public CTFontsListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "font"),
    };


    /**
     * Gets a List of "font" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont> getFontList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFontArray,
                this::setFontArray,
                this::insertNewFont,
                this::removeFont,
                this::sizeOfFontArray
            );
        }
    }

    /**
     * Gets array of all "font" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont[] getFontArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont[0]);
    }

    /**
     * Gets ith "font" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont getFontArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "font" element
     */
    @Override
    public int sizeOfFontArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "font" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFontArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont[] fontArray) {
        check_orphaned();
        arraySetterHelper(fontArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "font" element
     */
    @Override
    public void setFontArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont font) {
        generatedSetterHelperImpl(font, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "font" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont insertNewFont(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "font" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont addNewFont() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "font" element
     */
    @Override
    public void removeFont(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
