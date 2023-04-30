/*
 * XML Type:  CT_TLAnimateEffectBehavior
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLAnimateEffectBehavior(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLAnimateEffectBehaviorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior {
    private static final long serialVersionUID = 1L;

    public CTTLAnimateEffectBehaviorImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cBhvr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "progress"),
        new QName("", "transition"),
        new QName("", "filter"),
        new QName("", "prLst"),
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
     * Gets the "progress" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant getProgress() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "progress" element
     */
    @Override
    public boolean isSetProgress() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "progress" element
     */
    @Override
    public void setProgress(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant progress) {
        generatedSetterHelperImpl(progress, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "progress" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant addNewProgress() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "progress" element
     */
    @Override
    public void unsetProgress() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "transition" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition.Enum getTransition() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "transition" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition xgetTransition() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "transition" attribute
     */
    @Override
    public boolean isSetTransition() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "transition" attribute
     */
    @Override
    public void setTransition(org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition.Enum transition) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(transition);
        }
    }

    /**
     * Sets (as xml) the "transition" attribute
     */
    @Override
    public void xsetTransition(org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition transition) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(transition);
        }
    }

    /**
     * Unsets the "transition" attribute
     */
    @Override
    public void unsetTransition() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "filter" attribute
     */
    @Override
    public java.lang.String getFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "filter" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "filter" attribute
     */
    @Override
    public boolean isSetFilter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "filter" attribute
     */
    @Override
    public void setFilter(java.lang.String filter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(filter);
        }
    }

    /**
     * Sets (as xml) the "filter" attribute
     */
    @Override
    public void xsetFilter(org.apache.xmlbeans.XmlString filter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(filter);
        }
    }

    /**
     * Unsets the "filter" attribute
     */
    @Override
    public void unsetFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "prLst" attribute
     */
    @Override
    public java.lang.String getPrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "prLst" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetPrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "prLst" attribute
     */
    @Override
    public boolean isSetPrLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "prLst" attribute
     */
    @Override
    public void setPrLst(java.lang.String prLst) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setStringValue(prLst);
        }
    }

    /**
     * Sets (as xml) the "prLst" attribute
     */
    @Override
    public void xsetPrLst(org.apache.xmlbeans.XmlString prLst) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(prLst);
        }
    }

    /**
     * Unsets the "prLst" attribute
     */
    @Override
    public void unsetPrLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}
