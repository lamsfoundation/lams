/*
 * XML Type:  CT_Scenarios
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenarios
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Scenarios(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTScenariosImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenarios {
    private static final long serialVersionUID = 1L;

    public CTScenariosImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "scenario"),
        new QName("", "current"),
        new QName("", "show"),
        new QName("", "sqref"),
    };


    /**
     * Gets a List of "scenario" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario> getScenarioList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getScenarioArray,
                this::setScenarioArray,
                this::insertNewScenario,
                this::removeScenario,
                this::sizeOfScenarioArray
            );
        }
    }

    /**
     * Gets array of all "scenario" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario[] getScenarioArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario[0]);
    }

    /**
     * Gets ith "scenario" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario getScenarioArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "scenario" element
     */
    @Override
    public int sizeOfScenarioArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "scenario" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setScenarioArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario[] scenarioArray) {
        check_orphaned();
        arraySetterHelper(scenarioArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "scenario" element
     */
    @Override
    public void setScenarioArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario scenario) {
        generatedSetterHelperImpl(scenario, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "scenario" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario insertNewScenario(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "scenario" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario addNewScenario() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenario)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "scenario" element
     */
    @Override
    public void removeScenario(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "current" attribute
     */
    @Override
    public long getCurrent() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "current" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetCurrent() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "current" attribute
     */
    @Override
    public boolean isSetCurrent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "current" attribute
     */
    @Override
    public void setCurrent(long current) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(current);
        }
    }

    /**
     * Sets (as xml) the "current" attribute
     */
    @Override
    public void xsetCurrent(org.apache.xmlbeans.XmlUnsignedInt current) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(current);
        }
    }

    /**
     * Unsets the "current" attribute
     */
    @Override
    public void unsetCurrent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "show" attribute
     */
    @Override
    public long getShow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "show" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetShow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "show" attribute
     */
    @Override
    public boolean isSetShow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "show" attribute
     */
    @Override
    public void setShow(long show) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setLongValue(show);
        }
    }

    /**
     * Sets (as xml) the "show" attribute
     */
    @Override
    public void xsetShow(org.apache.xmlbeans.XmlUnsignedInt show) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(show);
        }
    }

    /**
     * Unsets the "show" attribute
     */
    @Override
    public void unsetShow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "sqref" attribute
     */
    @Override
    public java.util.List getSqref() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getListValue();
        }
    }

    /**
     * Gets (as xml) the "sqref" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref xgetSqref() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "sqref" attribute
     */
    @Override
    public boolean isSetSqref() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "sqref" attribute
     */
    @Override
    public void setSqref(java.util.List sqref) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setListValue(sqref);
        }
    }

    /**
     * Sets (as xml) the "sqref" attribute
     */
    @Override
    public void xsetSqref(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref sqref) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(sqref);
        }
    }

    /**
     * Unsets the "sqref" attribute
     */
    @Override
    public void unsetSqref() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}
