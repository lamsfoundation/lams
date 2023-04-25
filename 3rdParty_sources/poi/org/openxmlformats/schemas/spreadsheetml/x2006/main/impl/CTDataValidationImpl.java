/*
 * XML Type:  CT_DataValidation
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidation
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DataValidation(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTDataValidationImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidation {
    private static final long serialVersionUID = 1L;

    public CTDataValidationImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "formula1"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "formula2"),
        new QName("", "type"),
        new QName("", "errorStyle"),
        new QName("", "imeMode"),
        new QName("", "operator"),
        new QName("", "allowBlank"),
        new QName("", "showDropDown"),
        new QName("", "showInputMessage"),
        new QName("", "showErrorMessage"),
        new QName("", "errorTitle"),
        new QName("", "error"),
        new QName("", "promptTitle"),
        new QName("", "prompt"),
        new QName("", "sqref"),
    };


    /**
     * Gets the "formula1" element
     */
    @Override
    public java.lang.String getFormula1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "formula1" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula xgetFormula1() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * True if has "formula1" element
     */
    @Override
    public boolean isSetFormula1() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "formula1" element
     */
    @Override
    public void setFormula1(java.lang.String formula1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(formula1);
        }
    }

    /**
     * Sets (as xml) the "formula1" element
     */
    @Override
    public void xsetFormula1(org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula formula1) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(formula1);
        }
    }

    /**
     * Unsets the "formula1" element
     */
    @Override
    public void unsetFormula1() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "formula2" element
     */
    @Override
    public java.lang.String getFormula2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "formula2" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula xgetFormula2() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * True if has "formula2" element
     */
    @Override
    public boolean isSetFormula2() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "formula2" element
     */
    @Override
    public void setFormula2(java.lang.String formula2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(formula2);
        }
    }

    /**
     * Sets (as xml) the "formula2" element
     */
    @Override
    public void xsetFormula2(org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula formula2) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(formula2);
        }
    }

    /**
     * Unsets the "formula2" element
     */
    @Override
    public void unsetFormula2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType.Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType xgetType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "type" attribute
     */
    @Override
    public boolean isSetType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "type" attribute
     */
    @Override
    public void setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType.Enum type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(type);
        }
    }

    /**
     * Sets (as xml) the "type" attribute
     */
    @Override
    public void xsetType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType type) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(type);
        }
    }

    /**
     * Unsets the "type" attribute
     */
    @Override
    public void unsetType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "errorStyle" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle.Enum getErrorStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "errorStyle" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle xgetErrorStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "errorStyle" attribute
     */
    @Override
    public boolean isSetErrorStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "errorStyle" attribute
     */
    @Override
    public void setErrorStyle(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle.Enum errorStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(errorStyle);
        }
    }

    /**
     * Sets (as xml) the "errorStyle" attribute
     */
    @Override
    public void xsetErrorStyle(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle errorStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(errorStyle);
        }
    }

    /**
     * Unsets the "errorStyle" attribute
     */
    @Override
    public void unsetErrorStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "imeMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode.Enum getImeMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "imeMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode xgetImeMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "imeMode" attribute
     */
    @Override
    public boolean isSetImeMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "imeMode" attribute
     */
    @Override
    public void setImeMode(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode.Enum imeMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(imeMode);
        }
    }

    /**
     * Sets (as xml) the "imeMode" attribute
     */
    @Override
    public void xsetImeMode(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode imeMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(imeMode);
        }
    }

    /**
     * Unsets the "imeMode" attribute
     */
    @Override
    public void unsetImeMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "operator" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator.Enum getOperator() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "operator" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator xgetOperator() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "operator" attribute
     */
    @Override
    public boolean isSetOperator() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "operator" attribute
     */
    @Override
    public void setOperator(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator.Enum operator) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(operator);
        }
    }

    /**
     * Sets (as xml) the "operator" attribute
     */
    @Override
    public void xsetOperator(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator operator) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(operator);
        }
    }

    /**
     * Unsets the "operator" attribute
     */
    @Override
    public void unsetOperator() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "allowBlank" attribute
     */
    @Override
    public boolean getAllowBlank() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "allowBlank" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAllowBlank() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return target;
        }
    }

    /**
     * True if has "allowBlank" attribute
     */
    @Override
    public boolean isSetAllowBlank() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "allowBlank" attribute
     */
    @Override
    public void setAllowBlank(boolean allowBlank) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setBooleanValue(allowBlank);
        }
    }

    /**
     * Sets (as xml) the "allowBlank" attribute
     */
    @Override
    public void xsetAllowBlank(org.apache.xmlbeans.XmlBoolean allowBlank) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(allowBlank);
        }
    }

    /**
     * Unsets the "allowBlank" attribute
     */
    @Override
    public void unsetAllowBlank() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "showDropDown" attribute
     */
    @Override
    public boolean getShowDropDown() {
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
     * Gets (as xml) the "showDropDown" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowDropDown() {
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
     * True if has "showDropDown" attribute
     */
    @Override
    public boolean isSetShowDropDown() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "showDropDown" attribute
     */
    @Override
    public void setShowDropDown(boolean showDropDown) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setBooleanValue(showDropDown);
        }
    }

    /**
     * Sets (as xml) the "showDropDown" attribute
     */
    @Override
    public void xsetShowDropDown(org.apache.xmlbeans.XmlBoolean showDropDown) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(showDropDown);
        }
    }

    /**
     * Unsets the "showDropDown" attribute
     */
    @Override
    public void unsetShowDropDown() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "showInputMessage" attribute
     */
    @Override
    public boolean getShowInputMessage() {
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
     * Gets (as xml) the "showInputMessage" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowInputMessage() {
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
     * True if has "showInputMessage" attribute
     */
    @Override
    public boolean isSetShowInputMessage() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "showInputMessage" attribute
     */
    @Override
    public void setShowInputMessage(boolean showInputMessage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setBooleanValue(showInputMessage);
        }
    }

    /**
     * Sets (as xml) the "showInputMessage" attribute
     */
    @Override
    public void xsetShowInputMessage(org.apache.xmlbeans.XmlBoolean showInputMessage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(showInputMessage);
        }
    }

    /**
     * Unsets the "showInputMessage" attribute
     */
    @Override
    public void unsetShowInputMessage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "showErrorMessage" attribute
     */
    @Override
    public boolean getShowErrorMessage() {
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
     * Gets (as xml) the "showErrorMessage" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowErrorMessage() {
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
     * True if has "showErrorMessage" attribute
     */
    @Override
    public boolean isSetShowErrorMessage() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "showErrorMessage" attribute
     */
    @Override
    public void setShowErrorMessage(boolean showErrorMessage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setBooleanValue(showErrorMessage);
        }
    }

    /**
     * Sets (as xml) the "showErrorMessage" attribute
     */
    @Override
    public void xsetShowErrorMessage(org.apache.xmlbeans.XmlBoolean showErrorMessage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(showErrorMessage);
        }
    }

    /**
     * Unsets the "showErrorMessage" attribute
     */
    @Override
    public void unsetShowErrorMessage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "errorTitle" attribute
     */
    @Override
    public java.lang.String getErrorTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "errorTitle" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetErrorTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * True if has "errorTitle" attribute
     */
    @Override
    public boolean isSetErrorTitle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "errorTitle" attribute
     */
    @Override
    public void setErrorTitle(java.lang.String errorTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setStringValue(errorTitle);
        }
    }

    /**
     * Sets (as xml) the "errorTitle" attribute
     */
    @Override
    public void xsetErrorTitle(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring errorTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(errorTitle);
        }
    }

    /**
     * Unsets the "errorTitle" attribute
     */
    @Override
    public void unsetErrorTitle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "error" attribute
     */
    @Override
    public java.lang.String getError() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "error" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetError() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * True if has "error" attribute
     */
    @Override
    public boolean isSetError() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "error" attribute
     */
    @Override
    public void setError(java.lang.String error) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setStringValue(error);
        }
    }

    /**
     * Sets (as xml) the "error" attribute
     */
    @Override
    public void xsetError(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring error) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(error);
        }
    }

    /**
     * Unsets the "error" attribute
     */
    @Override
    public void unsetError() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "promptTitle" attribute
     */
    @Override
    public java.lang.String getPromptTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "promptTitle" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetPromptTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * True if has "promptTitle" attribute
     */
    @Override
    public boolean isSetPromptTitle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "promptTitle" attribute
     */
    @Override
    public void setPromptTitle(java.lang.String promptTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setStringValue(promptTitle);
        }
    }

    /**
     * Sets (as xml) the "promptTitle" attribute
     */
    @Override
    public void xsetPromptTitle(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring promptTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(promptTitle);
        }
    }

    /**
     * Unsets the "promptTitle" attribute
     */
    @Override
    public void unsetPromptTitle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "prompt" attribute
     */
    @Override
    public java.lang.String getPrompt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "prompt" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetPrompt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * True if has "prompt" attribute
     */
    @Override
    public boolean isSetPrompt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "prompt" attribute
     */
    @Override
    public void setPrompt(java.lang.String prompt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setStringValue(prompt);
        }
    }

    /**
     * Sets (as xml) the "prompt" attribute
     */
    @Override
    public void xsetPrompt(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring prompt) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(prompt);
        }
    }

    /**
     * Unsets the "prompt" attribute
     */
    @Override
    public void unsetPrompt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return target;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(sqref);
        }
    }
}
