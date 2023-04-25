/*
 * XML Type:  CT_Background
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Background(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTBackgroundImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTBackground {
    private static final long serialVersionUID = 1L;

    public CTBackgroundImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bgPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bgRef"),
        new QName("", "bwMode"),
    };


    /**
     * Gets the "bgPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties getBgPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bgPr" element
     */
    @Override
    public boolean isSetBgPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "bgPr" element
     */
    @Override
    public void setBgPr(org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties bgPr) {
        generatedSetterHelperImpl(bgPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bgPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties addNewBgPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "bgPr" element
     */
    @Override
    public void unsetBgPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "bgRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getBgRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bgRef" element
     */
    @Override
    public boolean isSetBgRef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "bgRef" element
     */
    @Override
    public void setBgRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference bgRef) {
        generatedSetterHelperImpl(bgRef, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bgRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewBgRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "bgRef" element
     */
    @Override
    public void unsetBgRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "bwMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum getBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "bwMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode xgetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "bwMode" attribute
     */
    @Override
    public boolean isSetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "bwMode" attribute
     */
    @Override
    public void setBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum bwMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(bwMode);
        }
    }

    /**
     * Sets (as xml) the "bwMode" attribute
     */
    @Override
    public void xsetBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode bwMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(bwMode);
        }
    }

    /**
     * Unsets the "bwMode" attribute
     */
    @Override
    public void unsetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}
