/*
 * XML Type:  CT_PosV
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTPosV
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PosV(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public class CTPosVImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTPosV {
    private static final long serialVersionUID = 1L;

    public CTPosVImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "align"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "posOffset"),
        new QName("", "relativeFrom"),
    };


    /**
     * Gets the "align" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV.Enum getAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "align" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV xgetAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * True if has "align" element
     */
    @Override
    public boolean isSetAlign() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "align" element
     */
    @Override
    public void setAlign(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV.Enum align) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(align);
        }
    }

    /**
     * Sets (as xml) the "align" element
     */
    @Override
    public void xsetAlign(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV align) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(align);
        }
    }

    /**
     * Unsets the "align" element
     */
    @Override
    public void unsetAlign() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "posOffset" element
     */
    @Override
    public int getPosOffset() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "posOffset" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STPositionOffset xgetPosOffset() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STPositionOffset target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STPositionOffset)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * True if has "posOffset" element
     */
    @Override
    public boolean isSetPosOffset() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "posOffset" element
     */
    @Override
    public void setPosOffset(int posOffset) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setIntValue(posOffset);
        }
    }

    /**
     * Sets (as xml) the "posOffset" element
     */
    @Override
    public void xsetPosOffset(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STPositionOffset posOffset) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STPositionOffset target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STPositionOffset)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STPositionOffset)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(posOffset);
        }
    }

    /**
     * Unsets the "posOffset" element
     */
    @Override
    public void unsetPosOffset() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "relativeFrom" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV.Enum getRelativeFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "relativeFrom" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV xgetRelativeFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "relativeFrom" attribute
     */
    @Override
    public void setRelativeFrom(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV.Enum relativeFrom) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(relativeFrom);
        }
    }

    /**
     * Sets (as xml) the "relativeFrom" attribute
     */
    @Override
    public void xsetRelativeFrom(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV relativeFrom) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(relativeFrom);
        }
    }
}
