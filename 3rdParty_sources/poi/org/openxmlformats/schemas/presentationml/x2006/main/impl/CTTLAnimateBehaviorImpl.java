/*
 * XML Type:  CT_TLAnimateBehavior
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLAnimateBehavior(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLAnimateBehaviorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior {
    private static final long serialVersionUID = 1L;

    public CTTLAnimateBehaviorImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cBhvr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "tavLst"),
        new QName("", "by"),
        new QName("", "from"),
        new QName("", "to"),
        new QName("", "calcmode"),
        new QName("", "valueType"),
    };


    /**
     * Gets the "cBhvr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData getCBhvr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cBhvr" element
     */
    @Override
    public void setCBhvr(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData cBhvr) {
        generatedSetterHelperImpl(cBhvr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cBhvr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData addNewCBhvr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "tavLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValueList getTavLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValueList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValueList)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tavLst" element
     */
    @Override
    public boolean isSetTavLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "tavLst" element
     */
    @Override
    public void setTavLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValueList tavLst) {
        generatedSetterHelperImpl(tavLst, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tavLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValueList addNewTavLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValueList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValueList)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "tavLst" element
     */
    @Override
    public void unsetTavLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "by" attribute
     */
    @Override
    public java.lang.String getBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "by" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "by" attribute
     */
    @Override
    public boolean isSetBy() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "by" attribute
     */
    @Override
    public void setBy(java.lang.String by) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(by);
        }
    }

    /**
     * Sets (as xml) the "by" attribute
     */
    @Override
    public void xsetBy(org.apache.xmlbeans.XmlString by) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(by);
        }
    }

    /**
     * Unsets the "by" attribute
     */
    @Override
    public void unsetBy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "from" attribute
     */
    @Override
    public java.lang.String getFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "from" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "from" attribute
     */
    @Override
    public boolean isSetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "from" attribute
     */
    @Override
    public void setFrom(java.lang.String from) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(from);
        }
    }

    /**
     * Sets (as xml) the "from" attribute
     */
    @Override
    public void xsetFrom(org.apache.xmlbeans.XmlString from) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(from);
        }
    }

    /**
     * Unsets the "from" attribute
     */
    @Override
    public void unsetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "to" attribute
     */
    @Override
    public java.lang.String getTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "to" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "to" attribute
     */
    @Override
    public boolean isSetTo() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "to" attribute
     */
    @Override
    public void setTo(java.lang.String to) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setStringValue(to);
        }
    }

    /**
     * Sets (as xml) the "to" attribute
     */
    @Override
    public void xsetTo(org.apache.xmlbeans.XmlString to) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(to);
        }
    }

    /**
     * Unsets the "to" attribute
     */
    @Override
    public void unsetTo() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "calcmode" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorCalcMode.Enum getCalcmode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorCalcMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "calcmode" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorCalcMode xgetCalcmode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorCalcMode target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorCalcMode)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "calcmode" attribute
     */
    @Override
    public boolean isSetCalcmode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "calcmode" attribute
     */
    @Override
    public void setCalcmode(org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorCalcMode.Enum calcmode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(calcmode);
        }
    }

    /**
     * Sets (as xml) the "calcmode" attribute
     */
    @Override
    public void xsetCalcmode(org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorCalcMode calcmode) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorCalcMode target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorCalcMode)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorCalcMode)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(calcmode);
        }
    }

    /**
     * Unsets the "calcmode" attribute
     */
    @Override
    public void unsetCalcmode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "valueType" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorValueType.Enum getValueType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorValueType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "valueType" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorValueType xgetValueType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorValueType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorValueType)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "valueType" attribute
     */
    @Override
    public boolean isSetValueType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "valueType" attribute
     */
    @Override
    public void setValueType(org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorValueType.Enum valueType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(valueType);
        }
    }

    /**
     * Sets (as xml) the "valueType" attribute
     */
    @Override
    public void xsetValueType(org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorValueType valueType) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorValueType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorValueType)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateBehaviorValueType)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(valueType);
        }
    }

    /**
     * Unsets the "valueType" attribute
     */
    @Override
    public void unsetValueType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }
}
