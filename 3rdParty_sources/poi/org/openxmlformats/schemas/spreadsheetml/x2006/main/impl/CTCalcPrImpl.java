/*
 * XML Type:  CT_CalcPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CalcPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTCalcPrImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcPr {
    private static final long serialVersionUID = 1L;

    public CTCalcPrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "calcId"),
        new QName("", "calcMode"),
        new QName("", "fullCalcOnLoad"),
        new QName("", "refMode"),
        new QName("", "iterate"),
        new QName("", "iterateCount"),
        new QName("", "iterateDelta"),
        new QName("", "fullPrecision"),
        new QName("", "calcCompleted"),
        new QName("", "calcOnSave"),
        new QName("", "concurrentCalc"),
        new QName("", "concurrentManualCount"),
        new QName("", "forceFullCalc"),
    };


    /**
     * Gets the "calcId" attribute
     */
    @Override
    public long getCalcId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "calcId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetCalcId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "calcId" attribute
     */
    @Override
    public boolean isSetCalcId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "calcId" attribute
     */
    @Override
    public void setCalcId(long calcId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setLongValue(calcId);
        }
    }

    /**
     * Sets (as xml) the "calcId" attribute
     */
    @Override
    public void xsetCalcId(org.apache.xmlbeans.XmlUnsignedInt calcId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(calcId);
        }
    }

    /**
     * Unsets the "calcId" attribute
     */
    @Override
    public void unsetCalcId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "calcMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode.Enum getCalcMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "calcMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode xgetCalcMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "calcMode" attribute
     */
    @Override
    public boolean isSetCalcMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "calcMode" attribute
     */
    @Override
    public void setCalcMode(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode.Enum calcMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(calcMode);
        }
    }

    /**
     * Sets (as xml) the "calcMode" attribute
     */
    @Override
    public void xsetCalcMode(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode calcMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(calcMode);
        }
    }

    /**
     * Unsets the "calcMode" attribute
     */
    @Override
    public void unsetCalcMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "fullCalcOnLoad" attribute
     */
    @Override
    public boolean getFullCalcOnLoad() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "fullCalcOnLoad" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFullCalcOnLoad() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "fullCalcOnLoad" attribute
     */
    @Override
    public boolean isSetFullCalcOnLoad() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "fullCalcOnLoad" attribute
     */
    @Override
    public void setFullCalcOnLoad(boolean fullCalcOnLoad) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBooleanValue(fullCalcOnLoad);
        }
    }

    /**
     * Sets (as xml) the "fullCalcOnLoad" attribute
     */
    @Override
    public void xsetFullCalcOnLoad(org.apache.xmlbeans.XmlBoolean fullCalcOnLoad) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(fullCalcOnLoad);
        }
    }

    /**
     * Unsets the "fullCalcOnLoad" attribute
     */
    @Override
    public void unsetFullCalcOnLoad() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "refMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STRefMode.Enum getRefMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRefMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "refMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STRefMode xgetRefMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STRefMode target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRefMode)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRefMode)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "refMode" attribute
     */
    @Override
    public boolean isSetRefMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "refMode" attribute
     */
    @Override
    public void setRefMode(org.openxmlformats.schemas.spreadsheetml.x2006.main.STRefMode.Enum refMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(refMode);
        }
    }

    /**
     * Sets (as xml) the "refMode" attribute
     */
    @Override
    public void xsetRefMode(org.openxmlformats.schemas.spreadsheetml.x2006.main.STRefMode refMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STRefMode target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRefMode)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRefMode)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(refMode);
        }
    }

    /**
     * Unsets the "refMode" attribute
     */
    @Override
    public void unsetRefMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "iterate" attribute
     */
    @Override
    public boolean getIterate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "iterate" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetIterate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "iterate" attribute
     */
    @Override
    public boolean isSetIterate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "iterate" attribute
     */
    @Override
    public void setIterate(boolean iterate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setBooleanValue(iterate);
        }
    }

    /**
     * Sets (as xml) the "iterate" attribute
     */
    @Override
    public void xsetIterate(org.apache.xmlbeans.XmlBoolean iterate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(iterate);
        }
    }

    /**
     * Unsets the "iterate" attribute
     */
    @Override
    public void unsetIterate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "iterateCount" attribute
     */
    @Override
    public long getIterateCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "iterateCount" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetIterateCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "iterateCount" attribute
     */
    @Override
    public boolean isSetIterateCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "iterateCount" attribute
     */
    @Override
    public void setIterateCount(long iterateCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setLongValue(iterateCount);
        }
    }

    /**
     * Sets (as xml) the "iterateCount" attribute
     */
    @Override
    public void xsetIterateCount(org.apache.xmlbeans.XmlUnsignedInt iterateCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(iterateCount);
        }
    }

    /**
     * Unsets the "iterateCount" attribute
     */
    @Override
    public void unsetIterateCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "iterateDelta" attribute
     */
    @Override
    public double getIterateDelta() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return (target == null) ? 0.0 : target.getDoubleValue();
        }
    }

    /**
     * Gets (as xml) the "iterateDelta" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlDouble xgetIterateDelta() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDouble)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return target;
        }
    }

    /**
     * True if has "iterateDelta" attribute
     */
    @Override
    public boolean isSetIterateDelta() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "iterateDelta" attribute
     */
    @Override
    public void setIterateDelta(double iterateDelta) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setDoubleValue(iterateDelta);
        }
    }

    /**
     * Sets (as xml) the "iterateDelta" attribute
     */
    @Override
    public void xsetIterateDelta(org.apache.xmlbeans.XmlDouble iterateDelta) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(iterateDelta);
        }
    }

    /**
     * Unsets the "iterateDelta" attribute
     */
    @Override
    public void unsetIterateDelta() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "fullPrecision" attribute
     */
    @Override
    public boolean getFullPrecision() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "fullPrecision" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFullPrecision() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return target;
        }
    }

    /**
     * True if has "fullPrecision" attribute
     */
    @Override
    public boolean isSetFullPrecision() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "fullPrecision" attribute
     */
    @Override
    public void setFullPrecision(boolean fullPrecision) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setBooleanValue(fullPrecision);
        }
    }

    /**
     * Sets (as xml) the "fullPrecision" attribute
     */
    @Override
    public void xsetFullPrecision(org.apache.xmlbeans.XmlBoolean fullPrecision) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(fullPrecision);
        }
    }

    /**
     * Unsets the "fullPrecision" attribute
     */
    @Override
    public void unsetFullPrecision() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "calcCompleted" attribute
     */
    @Override
    public boolean getCalcCompleted() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "calcCompleted" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCalcCompleted() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return target;
        }
    }

    /**
     * True if has "calcCompleted" attribute
     */
    @Override
    public boolean isSetCalcCompleted() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "calcCompleted" attribute
     */
    @Override
    public void setCalcCompleted(boolean calcCompleted) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setBooleanValue(calcCompleted);
        }
    }

    /**
     * Sets (as xml) the "calcCompleted" attribute
     */
    @Override
    public void xsetCalcCompleted(org.apache.xmlbeans.XmlBoolean calcCompleted) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(calcCompleted);
        }
    }

    /**
     * Unsets the "calcCompleted" attribute
     */
    @Override
    public void unsetCalcCompleted() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "calcOnSave" attribute
     */
    @Override
    public boolean getCalcOnSave() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "calcOnSave" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCalcOnSave() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return target;
        }
    }

    /**
     * True if has "calcOnSave" attribute
     */
    @Override
    public boolean isSetCalcOnSave() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "calcOnSave" attribute
     */
    @Override
    public void setCalcOnSave(boolean calcOnSave) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setBooleanValue(calcOnSave);
        }
    }

    /**
     * Sets (as xml) the "calcOnSave" attribute
     */
    @Override
    public void xsetCalcOnSave(org.apache.xmlbeans.XmlBoolean calcOnSave) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(calcOnSave);
        }
    }

    /**
     * Unsets the "calcOnSave" attribute
     */
    @Override
    public void unsetCalcOnSave() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "concurrentCalc" attribute
     */
    @Override
    public boolean getConcurrentCalc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[10]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "concurrentCalc" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetConcurrentCalc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[10]);
            }
            return target;
        }
    }

    /**
     * True if has "concurrentCalc" attribute
     */
    @Override
    public boolean isSetConcurrentCalc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "concurrentCalc" attribute
     */
    @Override
    public void setConcurrentCalc(boolean concurrentCalc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setBooleanValue(concurrentCalc);
        }
    }

    /**
     * Sets (as xml) the "concurrentCalc" attribute
     */
    @Override
    public void xsetConcurrentCalc(org.apache.xmlbeans.XmlBoolean concurrentCalc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(concurrentCalc);
        }
    }

    /**
     * Unsets the "concurrentCalc" attribute
     */
    @Override
    public void unsetConcurrentCalc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "concurrentManualCount" attribute
     */
    @Override
    public long getConcurrentManualCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "concurrentManualCount" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetConcurrentManualCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * True if has "concurrentManualCount" attribute
     */
    @Override
    public boolean isSetConcurrentManualCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "concurrentManualCount" attribute
     */
    @Override
    public void setConcurrentManualCount(long concurrentManualCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setLongValue(concurrentManualCount);
        }
    }

    /**
     * Sets (as xml) the "concurrentManualCount" attribute
     */
    @Override
    public void xsetConcurrentManualCount(org.apache.xmlbeans.XmlUnsignedInt concurrentManualCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(concurrentManualCount);
        }
    }

    /**
     * Unsets the "concurrentManualCount" attribute
     */
    @Override
    public void unsetConcurrentManualCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "forceFullCalc" attribute
     */
    @Override
    public boolean getForceFullCalc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "forceFullCalc" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetForceFullCalc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * True if has "forceFullCalc" attribute
     */
    @Override
    public boolean isSetForceFullCalc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "forceFullCalc" attribute
     */
    @Override
    public void setForceFullCalc(boolean forceFullCalc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setBooleanValue(forceFullCalc);
        }
    }

    /**
     * Sets (as xml) the "forceFullCalc" attribute
     */
    @Override
    public void xsetForceFullCalc(org.apache.xmlbeans.XmlBoolean forceFullCalc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(forceFullCalc);
        }
    }

    /**
     * Unsets the "forceFullCalc" attribute
     */
    @Override
    public void unsetForceFullCalc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }
}
