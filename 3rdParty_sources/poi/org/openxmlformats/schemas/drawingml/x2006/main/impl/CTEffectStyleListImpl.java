/*
 * XML Type:  CT_EffectStyleList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_EffectStyleList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTEffectStyleListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList {
    private static final long serialVersionUID = 1L;

    public CTEffectStyleListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "effectStyle"),
    };


    /**
     * Gets a List of "effectStyle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem> getEffectStyleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEffectStyleArray,
                this::setEffectStyleArray,
                this::insertNewEffectStyle,
                this::removeEffectStyle,
                this::sizeOfEffectStyleArray
            );
        }
    }

    /**
     * Gets array of all "effectStyle" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem[] getEffectStyleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem[0]);
    }

    /**
     * Gets ith "effectStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem getEffectStyleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "effectStyle" element
     */
    @Override
    public int sizeOfEffectStyleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "effectStyle" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEffectStyleArray(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem[] effectStyleArray) {
        check_orphaned();
        arraySetterHelper(effectStyleArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "effectStyle" element
     */
    @Override
    public void setEffectStyleArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem effectStyle) {
        generatedSetterHelperImpl(effectStyle, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "effectStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem insertNewEffectStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "effectStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem addNewEffectStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "effectStyle" element
     */
    @Override
    public void removeEffectStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
