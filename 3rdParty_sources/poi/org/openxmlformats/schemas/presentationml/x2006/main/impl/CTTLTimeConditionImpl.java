/*
 * XML Type:  CT_TLTimeCondition
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLTimeCondition(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLTimeConditionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition {
    private static final long serialVersionUID = 1L;

    public CTTLTimeConditionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "tgtEl"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "tn"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "rtn"),
        new QName("", "evt"),
        new QName("", "delay"),
    };


    /**
     * Gets the "tgtEl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement getTgtEl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tgtEl" element
     */
    @Override
    public boolean isSetTgtEl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "tgtEl" element
     */
    @Override
    public void setTgtEl(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement tgtEl) {
        generatedSetterHelperImpl(tgtEl, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tgtEl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement addNewTgtEl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "tgtEl" element
     */
    @Override
    public void unsetTgtEl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "tn" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerTimeNodeID getTn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerTimeNodeID target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerTimeNodeID)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tn" element
     */
    @Override
    public boolean isSetTn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "tn" element
     */
    @Override
    public void setTn(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerTimeNodeID tn) {
        generatedSetterHelperImpl(tn, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tn" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerTimeNodeID addNewTn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerTimeNodeID target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerTimeNodeID)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "tn" element
     */
    @Override
    public void unsetTn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "rtn" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerRuntimeNode getRtn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerRuntimeNode target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerRuntimeNode)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rtn" element
     */
    @Override
    public boolean isSetRtn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "rtn" element
     */
    @Override
    public void setRtn(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerRuntimeNode rtn) {
        generatedSetterHelperImpl(rtn, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rtn" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerRuntimeNode addNewRtn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerRuntimeNode target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerRuntimeNode)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "rtn" element
     */
    @Override
    public void unsetRtn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "evt" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent.Enum getEvt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "evt" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent xgetEvt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "evt" attribute
     */
    @Override
    public boolean isSetEvt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "evt" attribute
     */
    @Override
    public void setEvt(org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent.Enum evt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(evt);
        }
    }

    /**
     * Sets (as xml) the "evt" attribute
     */
    @Override
    public void xsetEvt(org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent evt) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(evt);
        }
    }

    /**
     * Unsets the "evt" attribute
     */
    @Override
    public void unsetEvt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "delay" attribute
     */
    @Override
    public java.lang.Object getDelay() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "delay" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTime xgetDelay() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTime target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "delay" attribute
     */
    @Override
    public boolean isSetDelay() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "delay" attribute
     */
    @Override
    public void setDelay(java.lang.Object delay) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setObjectValue(delay);
        }
    }

    /**
     * Sets (as xml) the "delay" attribute
     */
    @Override
    public void xsetDelay(org.openxmlformats.schemas.presentationml.x2006.main.STTLTime delay) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTime target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTime)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(delay);
        }
    }

    /**
     * Unsets the "delay" attribute
     */
    @Override
    public void unsetDelay() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}
