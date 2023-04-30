/*
 * XML Type:  CT_ErrBars
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBars
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ErrBars(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTErrBarsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBars {
    private static final long serialVersionUID = 1L;

    public CTErrBarsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "errDir"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "errBarType"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "errValType"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "noEndCap"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "plus"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "minus"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "val"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "spPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "errDir" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTErrDir getErrDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTErrDir target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTErrDir)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "errDir" element
     */
    @Override
    public boolean isSetErrDir() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "errDir" element
     */
    @Override
    public void setErrDir(org.openxmlformats.schemas.drawingml.x2006.chart.CTErrDir errDir) {
        generatedSetterHelperImpl(errDir, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "errDir" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTErrDir addNewErrDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTErrDir target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTErrDir)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "errDir" element
     */
    @Override
    public void unsetErrDir() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "errBarType" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBarType getErrBarType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBarType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBarType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "errBarType" element
     */
    @Override
    public void setErrBarType(org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBarType errBarType) {
        generatedSetterHelperImpl(errBarType, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "errBarType" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBarType addNewErrBarType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBarType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBarType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "errValType" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTErrValType getErrValType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTErrValType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTErrValType)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "errValType" element
     */
    @Override
    public void setErrValType(org.openxmlformats.schemas.drawingml.x2006.chart.CTErrValType errValType) {
        generatedSetterHelperImpl(errValType, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "errValType" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTErrValType addNewErrValType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTErrValType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTErrValType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets the "noEndCap" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getNoEndCap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noEndCap" element
     */
    @Override
    public boolean isSetNoEndCap() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "noEndCap" element
     */
    @Override
    public void setNoEndCap(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean noEndCap) {
        generatedSetterHelperImpl(noEndCap, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noEndCap" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewNoEndCap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "noEndCap" element
     */
    @Override
    public void unsetNoEndCap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "plus" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource getPlus() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "plus" element
     */
    @Override
    public boolean isSetPlus() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "plus" element
     */
    @Override
    public void setPlus(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource plus) {
        generatedSetterHelperImpl(plus, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "plus" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource addNewPlus() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "plus" element
     */
    @Override
    public void unsetPlus() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "minus" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource getMinus() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "minus" element
     */
    @Override
    public boolean isSetMinus() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "minus" element
     */
    @Override
    public void setMinus(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource minus) {
        generatedSetterHelperImpl(minus, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "minus" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource addNewMinus() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "minus" element
     */
    @Override
    public void unsetMinus() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "val" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble getVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "val" element
     */
    @Override
    public boolean isSetVal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "val" element
     */
    @Override
    public void setVal(org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble val) {
        generatedSetterHelperImpl(val, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "val" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble addNewVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "val" element
     */
    @Override
    public void unsetVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "spPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties getSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spPr" element
     */
    @Override
    public boolean isSetSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "spPr" element
     */
    @Override
    public void setSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties spPr) {
        generatedSetterHelperImpl(spPr, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties addNewSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "spPr" element
     */
    @Override
    public void unsetSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[8], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[8]);
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
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }
}
