/*
 * XML Type:  CT_PresetTextShape
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPresetTextShape
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PresetTextShape(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPresetTextShapeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTPresetTextShape {
    private static final long serialVersionUID = 1L;

    public CTPresetTextShapeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "avLst"),
        new QName("", "prst"),
    };


    /**
     * Gets the "avLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList getAvLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "avLst" element
     */
    @Override
    public boolean isSetAvLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "avLst" element
     */
    @Override
    public void setAvLst(org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList avLst) {
        generatedSetterHelperImpl(avLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "avLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList addNewAvLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "avLst" element
     */
    @Override
    public void unsetAvLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "prst" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType.Enum getPrst() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "prst" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType xgetPrst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "prst" attribute
     */
    @Override
    public void setPrst(org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType.Enum prst) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(prst);
        }
    }

    /**
     * Sets (as xml) the "prst" attribute
     */
    @Override
    public void xsetPrst(org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType prst) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(prst);
        }
    }
}
