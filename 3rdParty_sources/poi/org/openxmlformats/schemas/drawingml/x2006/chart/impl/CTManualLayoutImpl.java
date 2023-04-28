/*
 * XML Type:  CT_ManualLayout
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ManualLayout(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTManualLayoutImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout {
    private static final long serialVersionUID = 1L;

    public CTManualLayoutImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "layoutTarget"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "xMode"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "yMode"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "wMode"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "hMode"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "x"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "y"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "w"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "h"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "layoutTarget" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget getLayoutTarget() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "layoutTarget" element
     */
    @Override
    public boolean isSetLayoutTarget() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "layoutTarget" element
     */
    @Override
    public void setLayoutTarget(org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget layoutTarget) {
        generatedSetterHelperImpl(layoutTarget, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "layoutTarget" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget addNewLayoutTarget() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "layoutTarget" element
     */
    @Override
    public void unsetLayoutTarget() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "xMode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode getXMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "xMode" element
     */
    @Override
    public boolean isSetXMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "xMode" element
     */
    @Override
    public void setXMode(org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode xMode) {
        generatedSetterHelperImpl(xMode, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "xMode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode addNewXMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "xMode" element
     */
    @Override
    public void unsetXMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "yMode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode getYMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "yMode" element
     */
    @Override
    public boolean isSetYMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "yMode" element
     */
    @Override
    public void setYMode(org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode yMode) {
        generatedSetterHelperImpl(yMode, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "yMode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode addNewYMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "yMode" element
     */
    @Override
    public void unsetYMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "wMode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode getWMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "wMode" element
     */
    @Override
    public boolean isSetWMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "wMode" element
     */
    @Override
    public void setWMode(org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode wMode) {
        generatedSetterHelperImpl(wMode, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wMode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode addNewWMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "wMode" element
     */
    @Override
    public void unsetWMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "hMode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode getHMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hMode" element
     */
    @Override
    public boolean isSetHMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "hMode" element
     */
    @Override
    public void setHMode(org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode hMode) {
        generatedSetterHelperImpl(hMode, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hMode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode addNewHMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "hMode" element
     */
    @Override
    public void unsetHMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "x" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble getX() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "x" element
     */
    @Override
    public boolean isSetX() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "x" element
     */
    @Override
    public void setX(org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble x) {
        generatedSetterHelperImpl(x, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "x" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble addNewX() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "x" element
     */
    @Override
    public void unsetX() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "y" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble getY() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "y" element
     */
    @Override
    public boolean isSetY() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "y" element
     */
    @Override
    public void setY(org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble y) {
        generatedSetterHelperImpl(y, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "y" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble addNewY() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "y" element
     */
    @Override
    public void unsetY() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "w" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble getW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "w" element
     */
    @Override
    public boolean isSetW() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "w" element
     */
    @Override
    public void setW(org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble w) {
        generatedSetterHelperImpl(w, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "w" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble addNewW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "w" element
     */
    @Override
    public void unsetW() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "h" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble getH() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "h" element
     */
    @Override
    public boolean isSetH() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "h" element
     */
    @Override
    public void setH(org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble h) {
        generatedSetterHelperImpl(h, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "h" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble addNewH() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "h" element
     */
    @Override
    public void unsetH() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[9], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[9]);
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
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }
}
