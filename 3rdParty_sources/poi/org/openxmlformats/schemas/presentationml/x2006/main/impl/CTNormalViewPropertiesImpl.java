/*
 * XML Type:  CT_NormalViewProperties
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_NormalViewProperties(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTNormalViewPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewProperties {
    private static final long serialVersionUID = 1L;

    public CTNormalViewPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "restoredLeft"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "restoredTop"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
        new QName("", "showOutlineIcons"),
        new QName("", "snapVertSplitter"),
        new QName("", "vertBarState"),
        new QName("", "horzBarState"),
        new QName("", "preferSingleView"),
    };


    /**
     * Gets the "restoredLeft" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion getRestoredLeft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "restoredLeft" element
     */
    @Override
    public void setRestoredLeft(org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion restoredLeft) {
        generatedSetterHelperImpl(restoredLeft, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "restoredLeft" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion addNewRestoredLeft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "restoredTop" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion getRestoredTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "restoredTop" element
     */
    @Override
    public void setRestoredTop(org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion restoredTop) {
        generatedSetterHelperImpl(restoredTop, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "restoredTop" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion addNewRestoredTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "showOutlineIcons" attribute
     */
    @Override
    public boolean getShowOutlineIcons() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showOutlineIcons" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowOutlineIcons() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "showOutlineIcons" attribute
     */
    @Override
    public boolean isSetShowOutlineIcons() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "showOutlineIcons" attribute
     */
    @Override
    public void setShowOutlineIcons(boolean showOutlineIcons) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setBooleanValue(showOutlineIcons);
        }
    }

    /**
     * Sets (as xml) the "showOutlineIcons" attribute
     */
    @Override
    public void xsetShowOutlineIcons(org.apache.xmlbeans.XmlBoolean showOutlineIcons) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(showOutlineIcons);
        }
    }

    /**
     * Unsets the "showOutlineIcons" attribute
     */
    @Override
    public void unsetShowOutlineIcons() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "snapVertSplitter" attribute
     */
    @Override
    public boolean getSnapVertSplitter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "snapVertSplitter" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSnapVertSplitter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "snapVertSplitter" attribute
     */
    @Override
    public boolean isSetSnapVertSplitter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "snapVertSplitter" attribute
     */
    @Override
    public void setSnapVertSplitter(boolean snapVertSplitter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setBooleanValue(snapVertSplitter);
        }
    }

    /**
     * Sets (as xml) the "snapVertSplitter" attribute
     */
    @Override
    public void xsetSnapVertSplitter(org.apache.xmlbeans.XmlBoolean snapVertSplitter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(snapVertSplitter);
        }
    }

    /**
     * Unsets the "snapVertSplitter" attribute
     */
    @Override
    public void unsetSnapVertSplitter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "vertBarState" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState.Enum getVertBarState() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "vertBarState" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState xgetVertBarState() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "vertBarState" attribute
     */
    @Override
    public boolean isSetVertBarState() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "vertBarState" attribute
     */
    @Override
    public void setVertBarState(org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState.Enum vertBarState) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(vertBarState);
        }
    }

    /**
     * Sets (as xml) the "vertBarState" attribute
     */
    @Override
    public void xsetVertBarState(org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState vertBarState) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(vertBarState);
        }
    }

    /**
     * Unsets the "vertBarState" attribute
     */
    @Override
    public void unsetVertBarState() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "horzBarState" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState.Enum getHorzBarState() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "horzBarState" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState xgetHorzBarState() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return target;
        }
    }

    /**
     * True if has "horzBarState" attribute
     */
    @Override
    public boolean isSetHorzBarState() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "horzBarState" attribute
     */
    @Override
    public void setHorzBarState(org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState.Enum horzBarState) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(horzBarState);
        }
    }

    /**
     * Sets (as xml) the "horzBarState" attribute
     */
    @Override
    public void xsetHorzBarState(org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState horzBarState) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STSplitterBarState)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(horzBarState);
        }
    }

    /**
     * Unsets the "horzBarState" attribute
     */
    @Override
    public void unsetHorzBarState() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "preferSingleView" attribute
     */
    @Override
    public boolean getPreferSingleView() {
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
     * Gets (as xml) the "preferSingleView" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetPreferSingleView() {
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
     * True if has "preferSingleView" attribute
     */
    @Override
    public boolean isSetPreferSingleView() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "preferSingleView" attribute
     */
    @Override
    public void setPreferSingleView(boolean preferSingleView) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setBooleanValue(preferSingleView);
        }
    }

    /**
     * Sets (as xml) the "preferSingleView" attribute
     */
    @Override
    public void xsetPreferSingleView(org.apache.xmlbeans.XmlBoolean preferSingleView) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(preferSingleView);
        }
    }

    /**
     * Unsets the "preferSingleView" attribute
     */
    @Override
    public void unsetPreferSingleView() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }
}
