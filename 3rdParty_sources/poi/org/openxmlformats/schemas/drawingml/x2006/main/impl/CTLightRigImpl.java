/*
 * XML Type:  CT_LightRig
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTLightRig
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_LightRig(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTLightRigImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTLightRig {
    private static final long serialVersionUID = 1L;

    public CTLightRigImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "rot"),
        new QName("", "rig"),
        new QName("", "dir"),
    };


    /**
     * Gets the "rot" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords getRot() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rot" element
     */
    @Override
    public boolean isSetRot() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "rot" element
     */
    @Override
    public void setRot(org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords rot) {
        generatedSetterHelperImpl(rot, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rot" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords addNewRot() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "rot" element
     */
    @Override
    public void unsetRot() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "rig" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType.Enum getRig() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "rig" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType xgetRig() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "rig" attribute
     */
    @Override
    public void setRig(org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType.Enum rig) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(rig);
        }
    }

    /**
     * Sets (as xml) the "rig" attribute
     */
    @Override
    public void xsetRig(org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType rig) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(rig);
        }
    }

    /**
     * Gets the "dir" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection.Enum getDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "dir" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection xgetDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "dir" attribute
     */
    @Override
    public void setDir(org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection.Enum dir) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(dir);
        }
    }

    /**
     * Sets (as xml) the "dir" attribute
     */
    @Override
    public void xsetDir(org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection dir) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(dir);
        }
    }
}
