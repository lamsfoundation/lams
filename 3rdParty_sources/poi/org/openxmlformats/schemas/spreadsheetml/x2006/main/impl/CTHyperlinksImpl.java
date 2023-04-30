/*
 * XML Type:  CT_Hyperlinks
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlinks
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Hyperlinks(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTHyperlinksImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlinks {
    private static final long serialVersionUID = 1L;

    public CTHyperlinksImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "hyperlink"),
    };


    /**
     * Gets a List of "hyperlink" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink> getHyperlinkList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getHyperlinkArray,
                this::setHyperlinkArray,
                this::insertNewHyperlink,
                this::removeHyperlink,
                this::sizeOfHyperlinkArray
            );
        }
    }

    /**
     * Gets array of all "hyperlink" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink[] getHyperlinkArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink[0]);
    }

    /**
     * Gets ith "hyperlink" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink getHyperlinkArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "hyperlink" element
     */
    @Override
    public int sizeOfHyperlinkArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "hyperlink" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setHyperlinkArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink[] hyperlinkArray) {
        check_orphaned();
        arraySetterHelper(hyperlinkArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "hyperlink" element
     */
    @Override
    public void setHyperlinkArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink hyperlink) {
        generatedSetterHelperImpl(hyperlink, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "hyperlink" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink insertNewHyperlink(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "hyperlink" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink addNewHyperlink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "hyperlink" element
     */
    @Override
    public void removeHyperlink(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
