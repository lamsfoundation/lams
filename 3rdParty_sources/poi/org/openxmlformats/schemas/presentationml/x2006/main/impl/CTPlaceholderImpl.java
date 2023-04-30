/*
 * XML Type:  CT_Placeholder
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Placeholder(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTPlaceholderImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder {
    private static final long serialVersionUID = 1L;

    public CTPlaceholderImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
        new QName("", "type"),
        new QName("", "orient"),
        new QName("", "sz"),
        new QName("", "idx"),
        new QName("", "hasCustomPrompt"),
    };


    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType.Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType xgetType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType)get_default_attribute_value(PROPERTY_QNAME[1]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "type" attribute
     */
    @Override
    public void setType(org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType.Enum type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(type);
        }
    }

    /**
     * Sets (as xml) the "type" attribute
     */
    @Override
    public void xsetType(org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType type) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType)get_store().add_attribute_user(PROPERTY_QNAME[1]);
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
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "orient" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STDirection.Enum getOrient() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STDirection.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "orient" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STDirection xgetOrient() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STDirection target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STDirection)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STDirection)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "orient" attribute
     */
    @Override
    public boolean isSetOrient() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "orient" attribute
     */
    @Override
    public void setOrient(org.openxmlformats.schemas.presentationml.x2006.main.STDirection.Enum orient) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(orient);
        }
    }

    /**
     * Sets (as xml) the "orient" attribute
     */
    @Override
    public void xsetOrient(org.openxmlformats.schemas.presentationml.x2006.main.STDirection orient) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STDirection target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STDirection)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STDirection)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(orient);
        }
    }

    /**
     * Unsets the "orient" attribute
     */
    @Override
    public void unsetOrient() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "sz" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize.Enum getSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "sz" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize xgetSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "sz" attribute
     */
    @Override
    public boolean isSetSz() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "sz" attribute
     */
    @Override
    public void setSz(org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize.Enum sz) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(sz);
        }
    }

    /**
     * Sets (as xml) the "sz" attribute
     */
    @Override
    public void xsetSz(org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize sz) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(sz);
        }
    }

    /**
     * Unsets the "sz" attribute
     */
    @Override
    public void unsetSz() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "idx" attribute
     */
    @Override
    public long getIdx() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "idx" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetIdx() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "idx" attribute
     */
    @Override
    public boolean isSetIdx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "idx" attribute
     */
    @Override
    public void setIdx(long idx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setLongValue(idx);
        }
    }

    /**
     * Sets (as xml) the "idx" attribute
     */
    @Override
    public void xsetIdx(org.apache.xmlbeans.XmlUnsignedInt idx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(idx);
        }
    }

    /**
     * Unsets the "idx" attribute
     */
    @Override
    public void unsetIdx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "hasCustomPrompt" attribute
     */
    @Override
    public boolean getHasCustomPrompt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "hasCustomPrompt" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetHasCustomPrompt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "hasCustomPrompt" attribute
     */
    @Override
    public boolean isSetHasCustomPrompt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "hasCustomPrompt" attribute
     */
    @Override
    public void setHasCustomPrompt(boolean hasCustomPrompt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setBooleanValue(hasCustomPrompt);
        }
    }

    /**
     * Sets (as xml) the "hasCustomPrompt" attribute
     */
    @Override
    public void xsetHasCustomPrompt(org.apache.xmlbeans.XmlBoolean hasCustomPrompt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(hasCustomPrompt);
        }
    }

    /**
     * Unsets the "hasCustomPrompt" attribute
     */
    @Override
    public void unsetHasCustomPrompt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }
}
