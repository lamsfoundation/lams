/*
 * XML Type:  CT_EmbeddedFontList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_EmbeddedFontList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTEmbeddedFontListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontList {
    private static final long serialVersionUID = 1L;

    public CTEmbeddedFontListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "embeddedFont"),
    };


    /**
     * Gets a List of "embeddedFont" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry> getEmbeddedFontList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEmbeddedFontArray,
                this::setEmbeddedFontArray,
                this::insertNewEmbeddedFont,
                this::removeEmbeddedFont,
                this::sizeOfEmbeddedFontArray
            );
        }
    }

    /**
     * Gets array of all "embeddedFont" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry[] getEmbeddedFontArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry[0]);
    }

    /**
     * Gets ith "embeddedFont" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry getEmbeddedFontArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "embeddedFont" element
     */
    @Override
    public int sizeOfEmbeddedFontArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "embeddedFont" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEmbeddedFontArray(org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry[] embeddedFontArray) {
        check_orphaned();
        arraySetterHelper(embeddedFontArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "embeddedFont" element
     */
    @Override
    public void setEmbeddedFontArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry embeddedFont) {
        generatedSetterHelperImpl(embeddedFont, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "embeddedFont" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry insertNewEmbeddedFont(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "embeddedFont" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry addNewEmbeddedFont() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "embeddedFont" element
     */
    @Override
    public void removeEmbeddedFont(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
