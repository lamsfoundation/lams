/*
 * XML Type:  CT_StyleMatrix
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_StyleMatrix(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTStyleMatrixImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix {
    private static final long serialVersionUID = 1L;

    public CTStyleMatrixImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fillStyleLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lnStyleLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "effectStyleLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "bgFillStyleLst"),
        new QName("", "name"),
    };


    /**
     * Gets the "fillStyleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList getFillStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "fillStyleLst" element
     */
    @Override
    public void setFillStyleLst(org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList fillStyleLst) {
        generatedSetterHelperImpl(fillStyleLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fillStyleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList addNewFillStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "lnStyleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList getLnStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "lnStyleLst" element
     */
    @Override
    public void setLnStyleLst(org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList lnStyleLst) {
        generatedSetterHelperImpl(lnStyleLst, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lnStyleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList addNewLnStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "effectStyleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList getEffectStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "effectStyleLst" element
     */
    @Override
    public void setEffectStyleLst(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList effectStyleLst) {
        generatedSetterHelperImpl(effectStyleLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "effectStyleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList addNewEffectStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets the "bgFillStyleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFillStyleList getBgFillStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFillStyleList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFillStyleList)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "bgFillStyleLst" element
     */
    @Override
    public void setBgFillStyleLst(org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFillStyleList bgFillStyleLst) {
        generatedSetterHelperImpl(bgFillStyleLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bgFillStyleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFillStyleList addNewBgFillStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFillStyleList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFillStyleList)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Gets the "name" attribute
     */
    @Override
    public java.lang.String getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "name" attribute
     */
    @Override
    public boolean isSetName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "name" attribute
     */
    @Override
    public void setName(java.lang.String name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" attribute
     */
    @Override
    public void xsetName(org.apache.xmlbeans.XmlString name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(name);
        }
    }

    /**
     * Unsets the "name" attribute
     */
    @Override
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}
