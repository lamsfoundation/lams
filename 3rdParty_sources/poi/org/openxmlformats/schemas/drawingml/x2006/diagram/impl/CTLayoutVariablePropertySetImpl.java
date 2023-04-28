/*
 * XML Type:  CT_LayoutVariablePropertySet
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_LayoutVariablePropertySet(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTLayoutVariablePropertySetImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet {
    private static final long serialVersionUID = 1L;

    public CTLayoutVariablePropertySetImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "orgChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "chMax"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "chPref"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "bulletEnabled"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "dir"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "hierBranch"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "animOne"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "animLvl"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "resizeHandles"),
    };


    /**
     * Gets the "orgChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTOrgChart getOrgChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTOrgChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTOrgChart)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "orgChart" element
     */
    @Override
    public boolean isSetOrgChart() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "orgChart" element
     */
    @Override
    public void setOrgChart(org.openxmlformats.schemas.drawingml.x2006.diagram.CTOrgChart orgChart) {
        generatedSetterHelperImpl(orgChart, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "orgChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTOrgChart addNewOrgChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTOrgChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTOrgChart)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "orgChart" element
     */
    @Override
    public void unsetOrgChart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "chMax" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildMax getChMax() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildMax target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildMax)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "chMax" element
     */
    @Override
    public boolean isSetChMax() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "chMax" element
     */
    @Override
    public void setChMax(org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildMax chMax) {
        generatedSetterHelperImpl(chMax, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "chMax" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildMax addNewChMax() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildMax target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildMax)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "chMax" element
     */
    @Override
    public void unsetChMax() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "chPref" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildPref getChPref() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildPref target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildPref)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "chPref" element
     */
    @Override
    public boolean isSetChPref() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "chPref" element
     */
    @Override
    public void setChPref(org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildPref chPref) {
        generatedSetterHelperImpl(chPref, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "chPref" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildPref addNewChPref() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildPref target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTChildPref)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "chPref" element
     */
    @Override
    public void unsetChPref() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "bulletEnabled" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTBulletEnabled getBulletEnabled() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTBulletEnabled target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTBulletEnabled)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bulletEnabled" element
     */
    @Override
    public boolean isSetBulletEnabled() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "bulletEnabled" element
     */
    @Override
    public void setBulletEnabled(org.openxmlformats.schemas.drawingml.x2006.diagram.CTBulletEnabled bulletEnabled) {
        generatedSetterHelperImpl(bulletEnabled, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bulletEnabled" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTBulletEnabled addNewBulletEnabled() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTBulletEnabled target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTBulletEnabled)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "bulletEnabled" element
     */
    @Override
    public void unsetBulletEnabled() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "dir" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDirection getDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDirection target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDirection)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dir" element
     */
    @Override
    public boolean isSetDir() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "dir" element
     */
    @Override
    public void setDir(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDirection dir) {
        generatedSetterHelperImpl(dir, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dir" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDirection addNewDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDirection target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDirection)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "dir" element
     */
    @Override
    public void unsetDir() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "hierBranch" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTHierBranchStyle getHierBranch() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTHierBranchStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTHierBranchStyle)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hierBranch" element
     */
    @Override
    public boolean isSetHierBranch() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "hierBranch" element
     */
    @Override
    public void setHierBranch(org.openxmlformats.schemas.drawingml.x2006.diagram.CTHierBranchStyle hierBranch) {
        generatedSetterHelperImpl(hierBranch, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hierBranch" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTHierBranchStyle addNewHierBranch() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTHierBranchStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTHierBranchStyle)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "hierBranch" element
     */
    @Override
    public void unsetHierBranch() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "animOne" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimOne getAnimOne() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimOne target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimOne)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "animOne" element
     */
    @Override
    public boolean isSetAnimOne() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "animOne" element
     */
    @Override
    public void setAnimOne(org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimOne animOne) {
        generatedSetterHelperImpl(animOne, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "animOne" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimOne addNewAnimOne() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimOne target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimOne)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "animOne" element
     */
    @Override
    public void unsetAnimOne() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "animLvl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimLvl getAnimLvl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimLvl target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimLvl)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "animLvl" element
     */
    @Override
    public boolean isSetAnimLvl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "animLvl" element
     */
    @Override
    public void setAnimLvl(org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimLvl animLvl) {
        generatedSetterHelperImpl(animLvl, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "animLvl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimLvl addNewAnimLvl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimLvl target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTAnimLvl)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "animLvl" element
     */
    @Override
    public void unsetAnimLvl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "resizeHandles" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTResizeHandles getResizeHandles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTResizeHandles target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTResizeHandles)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "resizeHandles" element
     */
    @Override
    public boolean isSetResizeHandles() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "resizeHandles" element
     */
    @Override
    public void setResizeHandles(org.openxmlformats.schemas.drawingml.x2006.diagram.CTResizeHandles resizeHandles) {
        generatedSetterHelperImpl(resizeHandles, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "resizeHandles" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTResizeHandles addNewResizeHandles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTResizeHandles target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTResizeHandles)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "resizeHandles" element
     */
    @Override
    public void unsetResizeHandles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }
}
