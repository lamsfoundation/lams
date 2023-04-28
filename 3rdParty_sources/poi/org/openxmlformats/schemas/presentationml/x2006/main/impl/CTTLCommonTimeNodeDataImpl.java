/*
 * XML Type:  CT_TLCommonTimeNodeData
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLCommonTimeNodeData(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLCommonTimeNodeDataImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData {
    private static final long serialVersionUID = 1L;

    public CTTLCommonTimeNodeDataImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "stCondLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "endCondLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "endSync"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "iterate"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "childTnLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "subTnLst"),
        new QName("", "id"),
        new QName("", "presetID"),
        new QName("", "presetClass"),
        new QName("", "presetSubtype"),
        new QName("", "dur"),
        new QName("", "repeatCount"),
        new QName("", "repeatDur"),
        new QName("", "spd"),
        new QName("", "accel"),
        new QName("", "decel"),
        new QName("", "autoRev"),
        new QName("", "restart"),
        new QName("", "fill"),
        new QName("", "syncBehavior"),
        new QName("", "tmFilter"),
        new QName("", "evtFilter"),
        new QName("", "display"),
        new QName("", "masterRel"),
        new QName("", "bldLvl"),
        new QName("", "grpId"),
        new QName("", "afterEffect"),
        new QName("", "nodeType"),
        new QName("", "nodePh"),
    };


    /**
     * Gets the "stCondLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList getStCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "stCondLst" element
     */
    @Override
    public boolean isSetStCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "stCondLst" element
     */
    @Override
    public void setStCondLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList stCondLst) {
        generatedSetterHelperImpl(stCondLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "stCondLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList addNewStCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "stCondLst" element
     */
    @Override
    public void unsetStCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "endCondLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList getEndCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "endCondLst" element
     */
    @Override
    public boolean isSetEndCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "endCondLst" element
     */
    @Override
    public void setEndCondLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList endCondLst) {
        generatedSetterHelperImpl(endCondLst, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "endCondLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList addNewEndCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "endCondLst" element
     */
    @Override
    public void unsetEndCondLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "endSync" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition getEndSync() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "endSync" element
     */
    @Override
    public boolean isSetEndSync() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "endSync" element
     */
    @Override
    public void setEndSync(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition endSync) {
        generatedSetterHelperImpl(endSync, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "endSync" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition addNewEndSync() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "endSync" element
     */
    @Override
    public void unsetEndSync() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "iterate" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateData getIterate() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateData)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "iterate" element
     */
    @Override
    public boolean isSetIterate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "iterate" element
     */
    @Override
    public void setIterate(org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateData iterate) {
        generatedSetterHelperImpl(iterate, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "iterate" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateData addNewIterate() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateData)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "iterate" element
     */
    @Override
    public void unsetIterate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "childTnLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList getChildTnLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "childTnLst" element
     */
    @Override
    public boolean isSetChildTnLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "childTnLst" element
     */
    @Override
    public void setChildTnLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList childTnLst) {
        generatedSetterHelperImpl(childTnLst, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "childTnLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList addNewChildTnLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "childTnLst" element
     */
    @Override
    public void unsetChildTnLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "subTnLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList getSubTnLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "subTnLst" element
     */
    @Override
    public boolean isSetSubTnLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "subTnLst" element
     */
    @Override
    public void setSubTnLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList subTnLst) {
        generatedSetterHelperImpl(subTnLst, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "subTnLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList addNewSubTnLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "subTnLst" element
     */
    @Override
    public void unsetSubTnLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "id" attribute
     */
    @Override
    public long getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "id" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeID xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeID target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeID)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "id" attribute
     */
    @Override
    public boolean isSetId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "id" attribute
     */
    @Override
    public void setId(long id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setLongValue(id);
        }
    }

    /**
     * Sets (as xml) the "id" attribute
     */
    @Override
    public void xsetId(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeID id) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeID target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeID)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeID)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(id);
        }
    }

    /**
     * Unsets the "id" attribute
     */
    @Override
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "presetID" attribute
     */
    @Override
    public int getPresetID() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "presetID" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetPresetID() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "presetID" attribute
     */
    @Override
    public boolean isSetPresetID() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "presetID" attribute
     */
    @Override
    public void setPresetID(int presetID) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setIntValue(presetID);
        }
    }

    /**
     * Sets (as xml) the "presetID" attribute
     */
    @Override
    public void xsetPresetID(org.apache.xmlbeans.XmlInt presetID) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(presetID);
        }
    }

    /**
     * Unsets the "presetID" attribute
     */
    @Override
    public void unsetPresetID() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "presetClass" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType.Enum getPresetClass() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "presetClass" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType xgetPresetClass() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "presetClass" attribute
     */
    @Override
    public boolean isSetPresetClass() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "presetClass" attribute
     */
    @Override
    public void setPresetClass(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType.Enum presetClass) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setEnumValue(presetClass);
        }
    }

    /**
     * Sets (as xml) the "presetClass" attribute
     */
    @Override
    public void xsetPresetClass(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType presetClass) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(presetClass);
        }
    }

    /**
     * Unsets the "presetClass" attribute
     */
    @Override
    public void unsetPresetClass() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "presetSubtype" attribute
     */
    @Override
    public int getPresetSubtype() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "presetSubtype" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetPresetSubtype() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "presetSubtype" attribute
     */
    @Override
    public boolean isSetPresetSubtype() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "presetSubtype" attribute
     */
    @Override
    public void setPresetSubtype(int presetSubtype) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setIntValue(presetSubtype);
        }
    }

    /**
     * Sets (as xml) the "presetSubtype" attribute
     */
    @Override
    public void xsetPresetSubtype(org.apache.xmlbeans.XmlInt presetSubtype) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(presetSubtype);
        }
    }

    /**
     * Unsets the "presetSubtype" attribute
     */
    @Override
    public void unsetPresetSubtype() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "dur" attribute
     */
    @Override
    public java.lang.Object getDur() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "dur" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTime xgetDur() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTime target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * True if has "dur" attribute
     */
    @Override
    public boolean isSetDur() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "dur" attribute
     */
    @Override
    public void setDur(java.lang.Object dur) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setObjectValue(dur);
        }
    }

    /**
     * Sets (as xml) the "dur" attribute
     */
    @Override
    public void xsetDur(org.openxmlformats.schemas.presentationml.x2006.main.STTLTime dur) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTime target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(dur);
        }
    }

    /**
     * Unsets the "dur" attribute
     */
    @Override
    public void unsetDur() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "repeatCount" attribute
     */
    @Override
    public java.lang.Object getRepeatCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[11]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "repeatCount" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTime xgetRepeatCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTime target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_default_attribute_value(PROPERTY_QNAME[11]);
            }
            return target;
        }
    }

    /**
     * True if has "repeatCount" attribute
     */
    @Override
    public boolean isSetRepeatCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "repeatCount" attribute
     */
    @Override
    public void setRepeatCount(java.lang.Object repeatCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setObjectValue(repeatCount);
        }
    }

    /**
     * Sets (as xml) the "repeatCount" attribute
     */
    @Override
    public void xsetRepeatCount(org.openxmlformats.schemas.presentationml.x2006.main.STTLTime repeatCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTime target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(repeatCount);
        }
    }

    /**
     * Unsets the "repeatCount" attribute
     */
    @Override
    public void unsetRepeatCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "repeatDur" attribute
     */
    @Override
    public java.lang.Object getRepeatDur() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "repeatDur" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTime xgetRepeatDur() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTime target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * True if has "repeatDur" attribute
     */
    @Override
    public boolean isSetRepeatDur() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "repeatDur" attribute
     */
    @Override
    public void setRepeatDur(java.lang.Object repeatDur) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setObjectValue(repeatDur);
        }
    }

    /**
     * Sets (as xml) the "repeatDur" attribute
     */
    @Override
    public void xsetRepeatDur(org.openxmlformats.schemas.presentationml.x2006.main.STTLTime repeatDur) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTime target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(repeatDur);
        }
    }

    /**
     * Unsets the "repeatDur" attribute
     */
    @Override
    public void unsetRepeatDur() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "spd" attribute
     */
    @Override
    public java.lang.Object getSpd() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[13]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "spd" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetSpd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_default_attribute_value(PROPERTY_QNAME[13]);
            }
            return target;
        }
    }

    /**
     * True if has "spd" attribute
     */
    @Override
    public boolean isSetSpd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "spd" attribute
     */
    @Override
    public void setSpd(java.lang.Object spd) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setObjectValue(spd);
        }
    }

    /**
     * Sets (as xml) the "spd" attribute
     */
    @Override
    public void xsetSpd(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage spd) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(spd);
        }
    }

    /**
     * Unsets the "spd" attribute
     */
    @Override
    public void unsetSpd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "accel" attribute
     */
    @Override
    public java.lang.Object getAccel() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[14]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "accel" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage xgetAccel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage)get_default_attribute_value(PROPERTY_QNAME[14]);
            }
            return target;
        }
    }

    /**
     * True if has "accel" attribute
     */
    @Override
    public boolean isSetAccel() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "accel" attribute
     */
    @Override
    public void setAccel(java.lang.Object accel) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setObjectValue(accel);
        }
    }

    /**
     * Sets (as xml) the "accel" attribute
     */
    @Override
    public void xsetAccel(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage accel) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(accel);
        }
    }

    /**
     * Unsets the "accel" attribute
     */
    @Override
    public void unsetAccel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Gets the "decel" attribute
     */
    @Override
    public java.lang.Object getDecel() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "decel" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage xgetDecel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return target;
        }
    }

    /**
     * True if has "decel" attribute
     */
    @Override
    public boolean isSetDecel() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[15]) != null;
        }
    }

    /**
     * Sets the "decel" attribute
     */
    @Override
    public void setDecel(java.lang.Object decel) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setObjectValue(decel);
        }
    }

    /**
     * Sets (as xml) the "decel" attribute
     */
    @Override
    public void xsetDecel(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage decel) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(decel);
        }
    }

    /**
     * Unsets the "decel" attribute
     */
    @Override
    public void unsetDecel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Gets the "autoRev" attribute
     */
    @Override
    public boolean getAutoRev() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[16]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "autoRev" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAutoRev() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[16]);
            }
            return target;
        }
    }

    /**
     * True if has "autoRev" attribute
     */
    @Override
    public boolean isSetAutoRev() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[16]) != null;
        }
    }

    /**
     * Sets the "autoRev" attribute
     */
    @Override
    public void setAutoRev(boolean autoRev) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.setBooleanValue(autoRev);
        }
    }

    /**
     * Sets (as xml) the "autoRev" attribute
     */
    @Override
    public void xsetAutoRev(org.apache.xmlbeans.XmlBoolean autoRev) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.set(autoRev);
        }
    }

    /**
     * Unsets the "autoRev" attribute
     */
    @Override
    public void unsetAutoRev() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Gets the "restart" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeRestartType.Enum getRestart() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeRestartType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "restart" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeRestartType xgetRestart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeRestartType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeRestartType)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * True if has "restart" attribute
     */
    @Override
    public boolean isSetRestart() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[17]) != null;
        }
    }

    /**
     * Sets the "restart" attribute
     */
    @Override
    public void setRestart(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeRestartType.Enum restart) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.setEnumValue(restart);
        }
    }

    /**
     * Sets (as xml) the "restart" attribute
     */
    @Override
    public void xsetRestart(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeRestartType restart) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeRestartType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeRestartType)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeRestartType)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.set(restart);
        }
    }

    /**
     * Unsets the "restart" attribute
     */
    @Override
    public void unsetRestart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Gets the "fill" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeFillType.Enum getFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeFillType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "fill" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeFillType xgetFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeFillType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeFillType)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * True if has "fill" attribute
     */
    @Override
    public boolean isSetFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[18]) != null;
        }
    }

    /**
     * Sets the "fill" attribute
     */
    @Override
    public void setFill(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeFillType.Enum fill) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.setEnumValue(fill);
        }
    }

    /**
     * Sets (as xml) the "fill" attribute
     */
    @Override
    public void xsetFill(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeFillType fill) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeFillType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeFillType)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeFillType)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.set(fill);
        }
    }

    /**
     * Unsets the "fill" attribute
     */
    @Override
    public void unsetFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Gets the "syncBehavior" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeSyncType.Enum getSyncBehavior() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeSyncType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "syncBehavior" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeSyncType xgetSyncBehavior() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeSyncType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeSyncType)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * True if has "syncBehavior" attribute
     */
    @Override
    public boolean isSetSyncBehavior() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "syncBehavior" attribute
     */
    @Override
    public void setSyncBehavior(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeSyncType.Enum syncBehavior) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setEnumValue(syncBehavior);
        }
    }

    /**
     * Sets (as xml) the "syncBehavior" attribute
     */
    @Override
    public void xsetSyncBehavior(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeSyncType syncBehavior) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeSyncType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeSyncType)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeSyncType)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(syncBehavior);
        }
    }

    /**
     * Unsets the "syncBehavior" attribute
     */
    @Override
    public void unsetSyncBehavior() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "tmFilter" attribute
     */
    @Override
    public java.lang.String getTmFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "tmFilter" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetTmFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * True if has "tmFilter" attribute
     */
    @Override
    public boolean isSetTmFilter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "tmFilter" attribute
     */
    @Override
    public void setTmFilter(java.lang.String tmFilter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setStringValue(tmFilter);
        }
    }

    /**
     * Sets (as xml) the "tmFilter" attribute
     */
    @Override
    public void xsetTmFilter(org.apache.xmlbeans.XmlString tmFilter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(tmFilter);
        }
    }

    /**
     * Unsets the "tmFilter" attribute
     */
    @Override
    public void unsetTmFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Gets the "evtFilter" attribute
     */
    @Override
    public java.lang.String getEvtFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "evtFilter" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetEvtFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * True if has "evtFilter" attribute
     */
    @Override
    public boolean isSetEvtFilter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[21]) != null;
        }
    }

    /**
     * Sets the "evtFilter" attribute
     */
    @Override
    public void setEvtFilter(java.lang.String evtFilter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.setStringValue(evtFilter);
        }
    }

    /**
     * Sets (as xml) the "evtFilter" attribute
     */
    @Override
    public void xsetEvtFilter(org.apache.xmlbeans.XmlString evtFilter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.set(evtFilter);
        }
    }

    /**
     * Unsets the "evtFilter" attribute
     */
    @Override
    public void unsetEvtFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Gets the "display" attribute
     */
    @Override
    public boolean getDisplay() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "display" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDisplay() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * True if has "display" attribute
     */
    @Override
    public boolean isSetDisplay() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "display" attribute
     */
    @Override
    public void setDisplay(boolean display) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setBooleanValue(display);
        }
    }

    /**
     * Sets (as xml) the "display" attribute
     */
    @Override
    public void xsetDisplay(org.apache.xmlbeans.XmlBoolean display) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(display);
        }
    }

    /**
     * Unsets the "display" attribute
     */
    @Override
    public void unsetDisplay() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Gets the "masterRel" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeMasterRelation.Enum getMasterRel() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeMasterRelation.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "masterRel" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeMasterRelation xgetMasterRel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeMasterRelation target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeMasterRelation)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * True if has "masterRel" attribute
     */
    @Override
    public boolean isSetMasterRel() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[23]) != null;
        }
    }

    /**
     * Sets the "masterRel" attribute
     */
    @Override
    public void setMasterRel(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeMasterRelation.Enum masterRel) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.setEnumValue(masterRel);
        }
    }

    /**
     * Sets (as xml) the "masterRel" attribute
     */
    @Override
    public void xsetMasterRel(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeMasterRelation masterRel) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeMasterRelation target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeMasterRelation)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeMasterRelation)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.set(masterRel);
        }
    }

    /**
     * Unsets the "masterRel" attribute
     */
    @Override
    public void unsetMasterRel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Gets the "bldLvl" attribute
     */
    @Override
    public int getBldLvl() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "bldLvl" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetBldLvl() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * True if has "bldLvl" attribute
     */
    @Override
    public boolean isSetBldLvl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[24]) != null;
        }
    }

    /**
     * Sets the "bldLvl" attribute
     */
    @Override
    public void setBldLvl(int bldLvl) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.setIntValue(bldLvl);
        }
    }

    /**
     * Sets (as xml) the "bldLvl" attribute
     */
    @Override
    public void xsetBldLvl(org.apache.xmlbeans.XmlInt bldLvl) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.set(bldLvl);
        }
    }

    /**
     * Unsets the "bldLvl" attribute
     */
    @Override
    public void unsetBldLvl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Gets the "grpId" attribute
     */
    @Override
    public long getGrpId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "grpId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetGrpId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * True if has "grpId" attribute
     */
    @Override
    public boolean isSetGrpId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[25]) != null;
        }
    }

    /**
     * Sets the "grpId" attribute
     */
    @Override
    public void setGrpId(long grpId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.setLongValue(grpId);
        }
    }

    /**
     * Sets (as xml) the "grpId" attribute
     */
    @Override
    public void xsetGrpId(org.apache.xmlbeans.XmlUnsignedInt grpId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.set(grpId);
        }
    }

    /**
     * Unsets the "grpId" attribute
     */
    @Override
    public void unsetGrpId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Gets the "afterEffect" attribute
     */
    @Override
    public boolean getAfterEffect() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "afterEffect" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAfterEffect() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * True if has "afterEffect" attribute
     */
    @Override
    public boolean isSetAfterEffect() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[26]) != null;
        }
    }

    /**
     * Sets the "afterEffect" attribute
     */
    @Override
    public void setAfterEffect(boolean afterEffect) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.setBooleanValue(afterEffect);
        }
    }

    /**
     * Sets (as xml) the "afterEffect" attribute
     */
    @Override
    public void xsetAfterEffect(org.apache.xmlbeans.XmlBoolean afterEffect) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.set(afterEffect);
        }
    }

    /**
     * Unsets the "afterEffect" attribute
     */
    @Override
    public void unsetAfterEffect() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Gets the "nodeType" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeType.Enum getNodeType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "nodeType" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeType xgetNodeType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeType)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * True if has "nodeType" attribute
     */
    @Override
    public boolean isSetNodeType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[27]) != null;
        }
    }

    /**
     * Sets the "nodeType" attribute
     */
    @Override
    public void setNodeType(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeType.Enum nodeType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.setEnumValue(nodeType);
        }
    }

    /**
     * Sets (as xml) the "nodeType" attribute
     */
    @Override
    public void xsetNodeType(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeType nodeType) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeType)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodeType)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.set(nodeType);
        }
    }

    /**
     * Unsets the "nodeType" attribute
     */
    @Override
    public void unsetNodeType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[27]);
        }
    }

    /**
     * Gets the "nodePh" attribute
     */
    @Override
    public boolean getNodePh() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "nodePh" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetNodePh() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * True if has "nodePh" attribute
     */
    @Override
    public boolean isSetNodePh() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[28]) != null;
        }
    }

    /**
     * Sets the "nodePh" attribute
     */
    @Override
    public void setNodePh(boolean nodePh) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.setBooleanValue(nodePh);
        }
    }

    /**
     * Sets (as xml) the "nodePh" attribute
     */
    @Override
    public void xsetNodePh(org.apache.xmlbeans.XmlBoolean nodePh) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.set(nodePh);
        }
    }

    /**
     * Unsets the "nodePh" attribute
     */
    @Override
    public void unsetNodePh() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[28]);
        }
    }
}
