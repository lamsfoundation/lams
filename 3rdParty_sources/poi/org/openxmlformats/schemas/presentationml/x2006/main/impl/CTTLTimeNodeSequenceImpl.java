/*
 * XML Type:  CT_TLTimeNodeSequence
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLTimeNodeSequence(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLTimeNodeSequenceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence {
    private static final long serialVersionUID = 1L;

    public CTTLTimeNodeSequenceImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cTn"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "prevCondLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "nextCondLst"),
        new QName("", "concurrent"),
        new QName("", "prevAc"),
        new QName("", "nextAc"),
    };


    /**
     * Gets the "cTn" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData getCTn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cTn" element
     */
    @Override
    public void setCTn(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData cTn) {
        generatedSetterHelperImpl(cTn, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cTn" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData addNewCTn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "prevCondLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList getPrevCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "prevCondLst" element
     */
    @Override
    public boolean isSetPrevCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "prevCondLst" element
     */
    @Override
    public void setPrevCondLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList prevCondLst) {
        generatedSetterHelperImpl(prevCondLst, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "prevCondLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList addNewPrevCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "prevCondLst" element
     */
    @Override
    public void unsetPrevCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "nextCondLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList getNextCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "nextCondLst" element
     */
    @Override
    public boolean isSetNextCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "nextCondLst" element
     */
    @Override
    public void setNextCondLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList nextCondLst) {
        generatedSetterHelperImpl(nextCondLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "nextCondLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList addNewNextCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "nextCondLst" element
     */
    @Override
    public void unsetNextCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "concurrent" attribute
     */
    @Override
    public boolean getConcurrent() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "concurrent" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetConcurrent() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "concurrent" attribute
     */
    @Override
    public boolean isSetConcurrent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "concurrent" attribute
     */
    @Override
    public void setConcurrent(boolean concurrent) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setBooleanValue(concurrent);
        }
    }

    /**
     * Sets (as xml) the "concurrent" attribute
     */
    @Override
    public void xsetConcurrent(org.apache.xmlbeans.XmlBoolean concurrent) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(concurrent);
        }
    }

    /**
     * Unsets the "concurrent" attribute
     */
    @Override
    public void unsetConcurrent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "prevAc" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLPreviousActionType.Enum getPrevAc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLPreviousActionType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "prevAc" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLPreviousActionType xgetPrevAc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLPreviousActionType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLPreviousActionType)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "prevAc" attribute
     */
    @Override
    public boolean isSetPrevAc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "prevAc" attribute
     */
    @Override
    public void setPrevAc(org.openxmlformats.schemas.presentationml.x2006.main.STTLPreviousActionType.Enum prevAc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(prevAc);
        }
    }

    /**
     * Sets (as xml) the "prevAc" attribute
     */
    @Override
    public void xsetPrevAc(org.openxmlformats.schemas.presentationml.x2006.main.STTLPreviousActionType prevAc) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLPreviousActionType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLPreviousActionType)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLPreviousActionType)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(prevAc);
        }
    }

    /**
     * Unsets the "prevAc" attribute
     */
    @Override
    public void unsetPrevAc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "nextAc" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLNextActionType.Enum getNextAc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLNextActionType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "nextAc" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLNextActionType xgetNextAc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLNextActionType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLNextActionType)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "nextAc" attribute
     */
    @Override
    public boolean isSetNextAc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "nextAc" attribute
     */
    @Override
    public void setNextAc(org.openxmlformats.schemas.presentationml.x2006.main.STTLNextActionType.Enum nextAc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(nextAc);
        }
    }

    /**
     * Sets (as xml) the "nextAc" attribute
     */
    @Override
    public void xsetNextAc(org.openxmlformats.schemas.presentationml.x2006.main.STTLNextActionType nextAc) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLNextActionType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLNextActionType)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLNextActionType)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(nextAc);
        }
    }

    /**
     * Unsets the "nextAc" attribute
     */
    @Override
    public void unsetNextAc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }
}
