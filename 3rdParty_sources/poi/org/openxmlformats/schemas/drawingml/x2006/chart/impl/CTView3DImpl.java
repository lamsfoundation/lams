/*
 * XML Type:  CT_View3D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTView3D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_View3D(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTView3DImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTView3D {
    private static final long serialVersionUID = 1L;

    public CTView3DImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "rotX"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "hPercent"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "rotY"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "depthPercent"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "rAngAx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "perspective"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "rotX" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRotX getRotX() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRotX target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRotX)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rotX" element
     */
    @Override
    public boolean isSetRotX() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "rotX" element
     */
    @Override
    public void setRotX(org.openxmlformats.schemas.drawingml.x2006.chart.CTRotX rotX) {
        generatedSetterHelperImpl(rotX, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rotX" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRotX addNewRotX() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRotX target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRotX)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "rotX" element
     */
    @Override
    public void unsetRotX() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "hPercent" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTHPercent getHPercent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTHPercent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTHPercent)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hPercent" element
     */
    @Override
    public boolean isSetHPercent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "hPercent" element
     */
    @Override
    public void setHPercent(org.openxmlformats.schemas.drawingml.x2006.chart.CTHPercent hPercent) {
        generatedSetterHelperImpl(hPercent, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hPercent" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTHPercent addNewHPercent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTHPercent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTHPercent)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "hPercent" element
     */
    @Override
    public void unsetHPercent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "rotY" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRotY getRotY() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRotY target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRotY)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rotY" element
     */
    @Override
    public boolean isSetRotY() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "rotY" element
     */
    @Override
    public void setRotY(org.openxmlformats.schemas.drawingml.x2006.chart.CTRotY rotY) {
        generatedSetterHelperImpl(rotY, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rotY" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRotY addNewRotY() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRotY target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRotY)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "rotY" element
     */
    @Override
    public void unsetRotY() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "depthPercent" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDepthPercent getDepthPercent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDepthPercent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDepthPercent)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "depthPercent" element
     */
    @Override
    public boolean isSetDepthPercent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "depthPercent" element
     */
    @Override
    public void setDepthPercent(org.openxmlformats.schemas.drawingml.x2006.chart.CTDepthPercent depthPercent) {
        generatedSetterHelperImpl(depthPercent, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "depthPercent" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDepthPercent addNewDepthPercent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDepthPercent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDepthPercent)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "depthPercent" element
     */
    @Override
    public void unsetDepthPercent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "rAngAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getRAngAx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rAngAx" element
     */
    @Override
    public boolean isSetRAngAx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "rAngAx" element
     */
    @Override
    public void setRAngAx(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean rAngAx) {
        generatedSetterHelperImpl(rAngAx, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rAngAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewRAngAx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "rAngAx" element
     */
    @Override
    public void unsetRAngAx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "perspective" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPerspective getPerspective() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPerspective target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPerspective)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "perspective" element
     */
    @Override
    public boolean isSetPerspective() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "perspective" element
     */
    @Override
    public void setPerspective(org.openxmlformats.schemas.drawingml.x2006.chart.CTPerspective perspective) {
        generatedSetterHelperImpl(perspective, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "perspective" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPerspective addNewPerspective() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPerspective target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPerspective)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "perspective" element
     */
    @Override
    public void unsetPerspective() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }
}
