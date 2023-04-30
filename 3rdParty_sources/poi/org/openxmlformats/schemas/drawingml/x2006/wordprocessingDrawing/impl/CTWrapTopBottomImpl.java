/*
 * XML Type:  CT_WrapTopBottom
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWrapTopBottom
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_WrapTopBottom(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public class CTWrapTopBottomImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWrapTopBottom {
    private static final long serialVersionUID = 1L;

    public CTWrapTopBottomImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "effectExtent"),
        new QName("", "distT"),
        new QName("", "distB"),
    };


    /**
     * Gets the "effectExtent" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent getEffectExtent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "effectExtent" element
     */
    @Override
    public boolean isSetEffectExtent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "effectExtent" element
     */
    @Override
    public void setEffectExtent(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent effectExtent) {
        generatedSetterHelperImpl(effectExtent, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "effectExtent" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent addNewEffectExtent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "effectExtent" element
     */
    @Override
    public void unsetEffectExtent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "distT" attribute
     */
    @Override
    public long getDistT() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "distT" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance xgetDistT() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "distT" attribute
     */
    @Override
    public boolean isSetDistT() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "distT" attribute
     */
    @Override
    public void setDistT(long distT) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(distT);
        }
    }

    /**
     * Sets (as xml) the "distT" attribute
     */
    @Override
    public void xsetDistT(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance distT) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(distT);
        }
    }

    /**
     * Unsets the "distT" attribute
     */
    @Override
    public void unsetDistT() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "distB" attribute
     */
    @Override
    public long getDistB() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "distB" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance xgetDistB() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "distB" attribute
     */
    @Override
    public boolean isSetDistB() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "distB" attribute
     */
    @Override
    public void setDistB(long distB) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setLongValue(distB);
        }
    }

    /**
     * Sets (as xml) the "distB" attribute
     */
    @Override
    public void xsetDistB(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance distB) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(distB);
        }
    }

    /**
     * Unsets the "distB" attribute
     */
    @Override
    public void unsetDistB() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}
