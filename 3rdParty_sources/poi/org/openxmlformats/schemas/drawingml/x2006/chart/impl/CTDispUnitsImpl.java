/*
 * XML Type:  CT_DispUnits
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnits
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DispUnits(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTDispUnitsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnits {
    private static final long serialVersionUID = 1L;

    public CTDispUnitsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "custUnit"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "builtInUnit"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "dispUnitsLbl"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "custUnit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble getCustUnit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "custUnit" element
     */
    @Override
    public boolean isSetCustUnit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "custUnit" element
     */
    @Override
    public void setCustUnit(org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble custUnit) {
        generatedSetterHelperImpl(custUnit, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "custUnit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble addNewCustUnit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "custUnit" element
     */
    @Override
    public void unsetCustUnit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "builtInUnit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBuiltInUnit getBuiltInUnit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBuiltInUnit target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBuiltInUnit)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "builtInUnit" element
     */
    @Override
    public boolean isSetBuiltInUnit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "builtInUnit" element
     */
    @Override
    public void setBuiltInUnit(org.openxmlformats.schemas.drawingml.x2006.chart.CTBuiltInUnit builtInUnit) {
        generatedSetterHelperImpl(builtInUnit, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "builtInUnit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBuiltInUnit addNewBuiltInUnit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBuiltInUnit target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBuiltInUnit)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "builtInUnit" element
     */
    @Override
    public void unsetBuiltInUnit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "dispUnitsLbl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnitsLbl getDispUnitsLbl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnitsLbl target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnitsLbl)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dispUnitsLbl" element
     */
    @Override
    public boolean isSetDispUnitsLbl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "dispUnitsLbl" element
     */
    @Override
    public void setDispUnitsLbl(org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnitsLbl dispUnitsLbl) {
        generatedSetterHelperImpl(dispUnitsLbl, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dispUnitsLbl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnitsLbl addNewDispUnitsLbl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnitsLbl target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnitsLbl)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "dispUnitsLbl" element
     */
    @Override
    public void unsetDispUnitsLbl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}
