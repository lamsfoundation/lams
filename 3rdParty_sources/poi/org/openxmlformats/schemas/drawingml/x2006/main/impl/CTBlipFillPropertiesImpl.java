/*
 * XML Type:  CT_BlipFillProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_BlipFillProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTBlipFillPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties {
    private static final long serialVersionUID = 1L;

    public CTBlipFillPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blip"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "srcRect"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tile"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "stretch"),
        new QName("", "dpi"),
        new QName("", "rotWithShape"),
    };


    /**
     * Gets the "blip" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlip getBlip() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlip target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlip)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "blip" element
     */
    @Override
    public boolean isSetBlip() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "blip" element
     */
    @Override
    public void setBlip(org.openxmlformats.schemas.drawingml.x2006.main.CTBlip blip) {
        generatedSetterHelperImpl(blip, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "blip" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlip addNewBlip() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlip target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlip)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "blip" element
     */
    @Override
    public void unsetBlip() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "srcRect" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect getSrcRect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "srcRect" element
     */
    @Override
    public boolean isSetSrcRect() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "srcRect" element
     */
    @Override
    public void setSrcRect(org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect srcRect) {
        generatedSetterHelperImpl(srcRect, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "srcRect" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect addNewSrcRect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "srcRect" element
     */
    @Override
    public void unsetSrcRect() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "tile" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties getTile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tile" element
     */
    @Override
    public boolean isSetTile() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "tile" element
     */
    @Override
    public void setTile(org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties tile) {
        generatedSetterHelperImpl(tile, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tile" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties addNewTile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "tile" element
     */
    @Override
    public void unsetTile() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "stretch" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties getStretch() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "stretch" element
     */
    @Override
    public boolean isSetStretch() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "stretch" element
     */
    @Override
    public void setStretch(org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties stretch) {
        generatedSetterHelperImpl(stretch, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "stretch" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties addNewStretch() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "stretch" element
     */
    @Override
    public void unsetStretch() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "dpi" attribute
     */
    @Override
    public long getDpi() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "dpi" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetDpi() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "dpi" attribute
     */
    @Override
    public boolean isSetDpi() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "dpi" attribute
     */
    @Override
    public void setDpi(long dpi) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setLongValue(dpi);
        }
    }

    /**
     * Sets (as xml) the "dpi" attribute
     */
    @Override
    public void xsetDpi(org.apache.xmlbeans.XmlUnsignedInt dpi) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(dpi);
        }
    }

    /**
     * Unsets the "dpi" attribute
     */
    @Override
    public void unsetDpi() {
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
