/*
 * XML Type:  CT_ConditionalFormatting
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ConditionalFormatting(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTConditionalFormattingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting {
    private static final long serialVersionUID = 1L;

    public CTConditionalFormattingImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cfRule"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
        new QName("", "pivot"),
        new QName("", "sqref"),
    };


    /**
     * Gets a List of "cfRule" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule> getCfRuleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCfRuleArray,
                this::setCfRuleArray,
                this::insertNewCfRule,
                this::removeCfRule,
                this::sizeOfCfRuleArray
            );
        }
    }

    /**
     * Gets array of all "cfRule" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule[] getCfRuleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule[0]);
    }

    /**
     * Gets ith "cfRule" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule getCfRuleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cfRule" element
     */
    @Override
    public int sizeOfCfRuleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cfRule" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCfRuleArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule[] cfRuleArray) {
        check_orphaned();
        arraySetterHelper(cfRuleArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cfRule" element
     */
    @Override
    public void setCfRuleArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule cfRule) {
        generatedSetterHelperImpl(cfRule, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cfRule" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule insertNewCfRule(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cfRule" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule addNewCfRule() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cfRule" element
     */
    @Override
    public void removeCfRule(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "pivot" attribute
     */
    @Override
    public boolean getPivot() {
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
     * Gets (as xml) the "pivot" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetPivot() {
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
     * True if has "pivot" attribute
     */
    @Override
    public boolean isSetPivot() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "pivot" attribute
     */
    @Override
    public void setPivot(boolean pivot) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBooleanValue(pivot);
        }
    }

    /**
     * Sets (as xml) the "pivot" attribute
     */
    @Override
    public void xsetPivot(org.apache.xmlbeans.XmlBoolean pivot) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(pivot);
        }
    }

    /**
     * Unsets the "pivot" attribute
     */
    @Override
    public void unsetPivot() {
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
