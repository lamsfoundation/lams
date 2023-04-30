/*
 * XML Type:  CT_Rules
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Rules(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTRulesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules {
    private static final long serialVersionUID = 1L;

    public CTRulesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "rule"),
    };


    /**
     * Gets a List of "rule" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule> getRuleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRuleArray,
                this::setRuleArray,
                this::insertNewRule,
                this::removeRule,
                this::sizeOfRuleArray
            );
        }
    }

    /**
     * Gets array of all "rule" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule[] getRuleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule[0]);
    }

    /**
     * Gets ith "rule" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule getRuleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rule" element
     */
    @Override
    public int sizeOfRuleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "rule" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRuleArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule[] ruleArray) {
        check_orphaned();
        arraySetterHelper(ruleArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "rule" element
     */
    @Override
    public void setRuleArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule rule) {
        generatedSetterHelperImpl(rule, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rule" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule insertNewRule(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rule" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule addNewRule() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "rule" element
     */
    @Override
    public void removeRule(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
