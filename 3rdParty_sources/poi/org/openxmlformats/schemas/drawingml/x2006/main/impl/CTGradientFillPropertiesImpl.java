/*
 * XML Type:  CT_GradientFillProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GradientFillProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTGradientFillPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties {
    private static final long serialVersionUID = 1L;

    public CTGradientFillPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gsLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lin"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "path"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tileRect"),
        new QName("", "flip"),
        new QName("", "rotWithShape"),
    };


    /**
     * Gets the "gsLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList getGsLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "gsLst" element
     */
    @Override
    public boolean isSetGsLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "gsLst" element
     */
    @Override
    public void setGsLst(org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList gsLst) {
        generatedSetterHelperImpl(gsLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "gsLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList addNewGsLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "gsLst" element
     */
    @Override
    public void unsetGsLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "lin" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLinearShadeProperties getLin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLinearShadeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLinearShadeProperties)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lin" element
     */
    @Override
    public boolean isSetLin() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "lin" element
     */
    @Override
    public void setLin(org.openxmlformats.schemas.drawingml.x2006.main.CTLinearShadeProperties lin) {
        generatedSetterHelperImpl(lin, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lin" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLinearShadeProperties addNewLin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLinearShadeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLinearShadeProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "lin" element
     */
    @Override
    public void unsetLin() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "path" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties getPath() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "path" element
     */
    @Override
    public boolean isSetPath() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "path" element
     */
    @Override
    public void setPath(org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties path) {
        generatedSetterHelperImpl(path, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "path" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties addNewPath() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "path" element
     */
    @Override
    public void unsetPath() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "tileRect" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect getTileRect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tileRect" element
     */
    @Override
    public boolean isSetTileRect() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "tileRect" element
     */
    @Override
    public void setTileRect(org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect tileRect) {
        generatedSetterHelperImpl(tileRect, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tileRect" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect addNewTileRect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "tileRect" element
     */
    @Override
    public void unsetTileRect() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "flip" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode.Enum getFlip() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "flip" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode xgetFlip() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "flip" attribute
     */
    @Override
    public boolean isSetFlip() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "flip" attribute
     */
    @Override
    public void setFlip(org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode.Enum flip) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(flip);
        }
    }

    /**
     * Sets (as xml) the "flip" attribute
     */
    @Override
    public void xsetFlip(org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode flip) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(flip);
        }
    }

    /**
     * Unsets the "flip" attribute
     */
    @Override
    public void unsetFlip() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "rotWithShape" attribute
     */
    @Override
    public boolean getRotWithShape() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "rotWithShape" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetRotWithShape() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "rotWithShape" attribute
     */
    @Override
    public boolean isSetRotWithShape() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "rotWithShape" attribute
     */
    @Override
    public void setRotWithShape(boolean rotWithShape) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setBooleanValue(rotWithShape);
        }
    }

    /**
     * Sets (as xml) the "rotWithShape" attribute
     */
    @Override
    public void xsetRotWithShape(org.apache.xmlbeans.XmlBoolean rotWithShape) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(rotWithShape);
        }
    }

    /**
     * Unsets the "rotWithShape" attribute
     */
    @Override
    public void unsetRotWithShape() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }
}
