/*
 * XML Type:  CT_FontCollection
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FontCollection(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFontCollectionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection {
    private static final long serialVersionUID = 1L;

    public CTFontCollectionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "latin"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "ea"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "cs"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "font"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
    };


    /**
     * Gets the "latin" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getLatin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "latin" element
     */
    @Override
    public void setLatin(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont latin) {
        generatedSetterHelperImpl(latin, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "latin" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewLatin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "ea" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getEa() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "ea" element
     */
    @Override
    public void setEa(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont ea) {
        generatedSetterHelperImpl(ea, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ea" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewEa() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "cs" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getCs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cs" element
     */
    @Override
    public void setCs(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont cs) {
        generatedSetterHelperImpl(cs, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cs" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewCs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets a List of "font" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont> getFontList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont[] getFontArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont[0]);
    }

    /**
     * Gets ith "font" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont getFontArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont)get_store().find_element_user(PROPERTY_QNAME[3], i);
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
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "font" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFontArray(org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont[] fontArray) {
        check_orphaned();
        arraySetterHelper(fontArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "font" element
     */
    @Override
    public void setFontArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont font) {
        generatedSetterHelperImpl(font, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "font" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont insertNewFont(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "font" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont addNewFont() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], i);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[4], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }
}
