/*
 * XML Type:  CT_FilterColumn
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilterColumn
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FilterColumn(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTFilterColumnImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilterColumn {
    private static final long serialVersionUID = 1L;

    public CTFilterColumnImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "filters"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "top10"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "customFilters"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "dynamicFilter"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "colorFilter"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "iconFilter"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
        new QName("", "colId"),
        new QName("", "hiddenButton"),
        new QName("", "showButton"),
    };


    /**
     * Gets the "filters" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters getFilters() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "filters" element
     */
    @Override
    public boolean isSetFilters() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "filters" element
     */
    @Override
    public void setFilters(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters filters) {
        generatedSetterHelperImpl(filters, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "filters" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters addNewFilters() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "filters" element
     */
    @Override
    public void unsetFilters() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "top10" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTop10 getTop10() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTop10 target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTop10)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "top10" element
     */
    @Override
    public boolean isSetTop10() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "top10" element
     */
    @Override
    public void setTop10(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTop10 top10) {
        generatedSetterHelperImpl(top10, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "top10" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTop10 addNewTop10() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTop10 target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTop10)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "top10" element
     */
    @Override
    public void unsetTop10() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "customFilters" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilters getCustomFilters() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilters target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilters)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "customFilters" element
     */
    @Override
    public boolean isSetCustomFilters() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "customFilters" element
     */
    @Override
    public void setCustomFilters(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilters customFilters) {
        generatedSetterHelperImpl(customFilters, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "customFilters" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilters addNewCustomFilters() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilters target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilters)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "customFilters" element
     */
    @Override
    public void unsetCustomFilters() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "dynamicFilter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDynamicFilter getDynamicFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDynamicFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDynamicFilter)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dynamicFilter" element
     */
    @Override
    public boolean isSetDynamicFilter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "dynamicFilter" element
     */
    @Override
    public void setDynamicFilter(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDynamicFilter dynamicFilter) {
        generatedSetterHelperImpl(dynamicFilter, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dynamicFilter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDynamicFilter addNewDynamicFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDynamicFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDynamicFilter)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "dynamicFilter" element
     */
    @Override
    public void unsetDynamicFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "colorFilter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorFilter getColorFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorFilter)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "colorFilter" element
     */
    @Override
    public boolean isSetColorFilter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "colorFilter" element
     */
    @Override
    public void setColorFilter(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorFilter colorFilter) {
        generatedSetterHelperImpl(colorFilter, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "colorFilter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorFilter addNewColorFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorFilter)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "colorFilter" element
     */
    @Override
    public void unsetColorFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "iconFilter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconFilter getIconFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconFilter)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "iconFilter" element
     */
    @Override
    public boolean isSetIconFilter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "iconFilter" element
     */
    @Override
    public void setIconFilter(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconFilter iconFilter) {
        generatedSetterHelperImpl(iconFilter, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "iconFilter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconFilter addNewIconFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconFilter)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "iconFilter" element
     */
    @Override
    public void unsetIconFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[6], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[6]);
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
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "colId" attribute
     */
    @Override
    public long getColId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "colId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetColId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Sets the "colId" attribute
     */
    @Override
    public void setColId(long colId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setLongValue(colId);
        }
    }

    /**
     * Sets (as xml) the "colId" attribute
     */
    @Override
    public void xsetColId(org.apache.xmlbeans.XmlUnsignedInt colId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(colId);
        }
    }

    /**
     * Gets the "hiddenButton" attribute
     */
    @Override
    public boolean getHiddenButton() {
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
     * Gets (as xml) the "hiddenButton" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetHiddenButton() {
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
     * True if has "hiddenButton" attribute
     */
    @Override
    public boolean isSetHiddenButton() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "hiddenButton" attribute
     */
    @Override
    public void setHiddenButton(boolean hiddenButton) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setBooleanValue(hiddenButton);
        }
    }

    /**
     * Sets (as xml) the "hiddenButton" attribute
     */
    @Override
    public void xsetHiddenButton(org.apache.xmlbeans.XmlBoolean hiddenButton) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(hiddenButton);
        }
    }

    /**
     * Unsets the "hiddenButton" attribute
     */
    @Override
    public void unsetHiddenButton() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "showButton" attribute
     */
    @Override
    public boolean getShowButton() {
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
     * Gets (as xml) the "showButton" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowButton() {
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
     * True if has "showButton" attribute
     */
    @Override
    public boolean isSetShowButton() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "showButton" attribute
     */
    @Override
    public void setShowButton(boolean showButton) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setBooleanValue(showButton);
        }
    }

    /**
     * Sets (as xml) the "showButton" attribute
     */
    @Override
    public void xsetShowButton(org.apache.xmlbeans.XmlBoolean showButton) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(showButton);
        }
    }

    /**
     * Unsets the "showButton" attribute
     */
    @Override
    public void unsetShowButton() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }
}
