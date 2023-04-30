/*
 * XML Type:  CT_SurfaceSer
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceSer
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SurfaceSer(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTSurfaceSerImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceSer {
    private static final long serialVersionUID = 1L;

    public CTSurfaceSerImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "idx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "order"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "tx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "spPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "cat"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "val"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "idx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getIdx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "idx" element
     */
    @Override
    public void setIdx(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt idx) {
        generatedSetterHelperImpl(idx, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "idx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewIdx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "order" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getOrder() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "order" element
     */
    @Override
    public void setOrder(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt order) {
        generatedSetterHelperImpl(order, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "order" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewOrder() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "tx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx getTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tx" element
     */
    @Override
    public boolean isSetTx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "tx" element
     */
    @Override
    public void setTx(org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx tx) {
        generatedSetterHelperImpl(tx, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx addNewTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "tx" element
     */
    @Override
    public void unsetTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "spPr" element
     */
    @Override
    public void setSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties spPr) {
        generatedSetterHelperImpl(spPr, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties addNewSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "cat" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource getCat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cat" element
     */
    @Override
    public boolean isSetCat() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "cat" element
     */
    @Override
    public void setCat(org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource cat) {
        generatedSetterHelperImpl(cat, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cat" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource addNewCat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "cat" element
     */
    @Override
    public void unsetCat() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "val" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource getVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource)get_store().find_element_user(PROPERTY_QNAME[5], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "val" element
     */
    @Override
    public void setVal(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource val) {
        generatedSetterHelperImpl(val, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "val" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource addNewVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource)get_store().add_element_user(PROPERTY_QNAME[5]);
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
