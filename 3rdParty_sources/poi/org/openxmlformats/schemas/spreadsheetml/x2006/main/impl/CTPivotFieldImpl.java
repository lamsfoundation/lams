/*
 * XML Type:  CT_PivotField
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PivotField(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTPivotFieldImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField {
    private static final long serialVersionUID = 1L;

    public CTPivotFieldImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "items"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "autoSortScope"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
        new QName("", "name"),
        new QName("", "axis"),
        new QName("", "dataField"),
        new QName("", "subtotalCaption"),
        new QName("", "showDropDowns"),
        new QName("", "hiddenLevel"),
        new QName("", "uniqueMemberProperty"),
        new QName("", "compact"),
        new QName("", "allDrilled"),
        new QName("", "numFmtId"),
        new QName("", "outline"),
        new QName("", "subtotalTop"),
        new QName("", "dragToRow"),
        new QName("", "dragToCol"),
        new QName("", "multipleItemSelectionAllowed"),
        new QName("", "dragToPage"),
        new QName("", "dragToData"),
        new QName("", "dragOff"),
        new QName("", "showAll"),
        new QName("", "insertBlankRow"),
        new QName("", "serverField"),
        new QName("", "insertPageBreak"),
        new QName("", "autoShow"),
        new QName("", "topAutoShow"),
        new QName("", "hideNewItems"),
        new QName("", "measureFilter"),
        new QName("", "includeNewItemsInFilter"),
        new QName("", "itemPageCount"),
        new QName("", "sortType"),
        new QName("", "dataSourceSort"),
        new QName("", "nonAutoSortDefault"),
        new QName("", "rankBy"),
        new QName("", "defaultSubtotal"),
        new QName("", "sumSubtotal"),
        new QName("", "countASubtotal"),
        new QName("", "avgSubtotal"),
        new QName("", "maxSubtotal"),
        new QName("", "minSubtotal"),
        new QName("", "productSubtotal"),
        new QName("", "countSubtotal"),
        new QName("", "stdDevSubtotal"),
        new QName("", "stdDevPSubtotal"),
        new QName("", "varSubtotal"),
        new QName("", "varPSubtotal"),
        new QName("", "showPropCell"),
        new QName("", "showPropTip"),
        new QName("", "showPropAsCaption"),
        new QName("", "defaultAttributeDrillState"),
    };


    /**
     * Gets the "items" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItems getItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItems)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "items" element
     */
    @Override
    public boolean isSetItems() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "items" element
     */
    @Override
    public void setItems(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItems items) {
        generatedSetterHelperImpl(items, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "items" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItems addNewItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItems)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "items" element
     */
    @Override
    public void unsetItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "autoSortScope" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoSortScope getAutoSortScope() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoSortScope target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoSortScope)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "autoSortScope" element
     */
    @Override
    public boolean isSetAutoSortScope() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "autoSortScope" element
     */
    @Override
    public void setAutoSortScope(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoSortScope autoSortScope) {
        generatedSetterHelperImpl(autoSortScope, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "autoSortScope" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoSortScope addNewAutoSortScope() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoSortScope target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoSortScope)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "autoSortScope" element
     */
    @Override
    public void unsetAutoSortScope() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[2]);
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
     * Gets the "name" attribute
     */
    @Override
    public java.lang.String getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "name" attribute
     */
    @Override
    public boolean isSetName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "name" attribute
     */
    @Override
    public void setName(java.lang.String name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" attribute
     */
    @Override
    public void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(name);
        }
    }

    /**
     * Unsets the "name" attribute
     */
    @Override
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "axis" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis.Enum getAxis() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "axis" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis xgetAxis() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "axis" attribute
     */
    @Override
    public boolean isSetAxis() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "axis" attribute
     */
    @Override
    public void setAxis(org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis.Enum axis) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(axis);
        }
    }

    /**
     * Sets (as xml) the "axis" attribute
     */
    @Override
    public void xsetAxis(org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis axis) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(axis);
        }
    }

    /**
     * Unsets the "axis" attribute
     */
    @Override
    public void unsetAxis() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "dataField" attribute
     */
    @Override
    public boolean getDataField() {
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
     * Gets (as xml) the "dataField" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDataField() {
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
     * True if has "dataField" attribute
     */
    @Override
    public boolean isSetDataField() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "dataField" attribute
     */
    @Override
    public void setDataField(boolean dataField) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setBooleanValue(dataField);
        }
    }

    /**
     * Sets (as xml) the "dataField" attribute
     */
    @Override
    public void xsetDataField(org.apache.xmlbeans.XmlBoolean dataField) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(dataField);
        }
    }

    /**
     * Unsets the "dataField" attribute
     */
    @Override
    public void unsetDataField() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "subtotalCaption" attribute
     */
    @Override
    public java.lang.String getSubtotalCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "subtotalCaption" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetSubtotalCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "subtotalCaption" attribute
     */
    @Override
    public boolean isSetSubtotalCaption() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "subtotalCaption" attribute
     */
    @Override
    public void setSubtotalCaption(java.lang.String subtotalCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setStringValue(subtotalCaption);
        }
    }

    /**
     * Sets (as xml) the "subtotalCaption" attribute
     */
    @Override
    public void xsetSubtotalCaption(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring subtotalCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(subtotalCaption);
        }
    }

    /**
     * Unsets the "subtotalCaption" attribute
     */
    @Override
    public void unsetSubtotalCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "showDropDowns" attribute
     */
    @Override
    public boolean getShowDropDowns() {
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
     * Gets (as xml) the "showDropDowns" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowDropDowns() {
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
     * True if has "showDropDowns" attribute
     */
    @Override
    public boolean isSetShowDropDowns() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "showDropDowns" attribute
     */
    @Override
    public void setShowDropDowns(boolean showDropDowns) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setBooleanValue(showDropDowns);
        }
    }

    /**
     * Sets (as xml) the "showDropDowns" attribute
     */
    @Override
    public void xsetShowDropDowns(org.apache.xmlbeans.XmlBoolean showDropDowns) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(showDropDowns);
        }
    }

    /**
     * Unsets the "showDropDowns" attribute
     */
    @Override
    public void unsetShowDropDowns() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "hiddenLevel" attribute
     */
    @Override
    public boolean getHiddenLevel() {
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
     * Gets (as xml) the "hiddenLevel" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetHiddenLevel() {
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
     * True if has "hiddenLevel" attribute
     */
    @Override
    public boolean isSetHiddenLevel() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "hiddenLevel" attribute
     */
    @Override
    public void setHiddenLevel(boolean hiddenLevel) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setBooleanValue(hiddenLevel);
        }
    }

    /**
     * Sets (as xml) the "hiddenLevel" attribute
     */
    @Override
    public void xsetHiddenLevel(org.apache.xmlbeans.XmlBoolean hiddenLevel) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(hiddenLevel);
        }
    }

    /**
     * Unsets the "hiddenLevel" attribute
     */
    @Override
    public void unsetHiddenLevel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "uniqueMemberProperty" attribute
     */
    @Override
    public java.lang.String getUniqueMemberProperty() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "uniqueMemberProperty" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetUniqueMemberProperty() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "uniqueMemberProperty" attribute
     */
    @Override
    public boolean isSetUniqueMemberProperty() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "uniqueMemberProperty" attribute
     */
    @Override
    public void setUniqueMemberProperty(java.lang.String uniqueMemberProperty) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setStringValue(uniqueMemberProperty);
        }
    }

    /**
     * Sets (as xml) the "uniqueMemberProperty" attribute
     */
    @Override
    public void xsetUniqueMemberProperty(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring uniqueMemberProperty) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(uniqueMemberProperty);
        }
    }

    /**
     * Unsets the "uniqueMemberProperty" attribute
     */
    @Override
    public void unsetUniqueMemberProperty() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "compact" attribute
     */
    @Override
    public boolean getCompact() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[10]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "compact" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCompact() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[10]);
            }
            return target;
        }
    }

    /**
     * True if has "compact" attribute
     */
    @Override
    public boolean isSetCompact() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "compact" attribute
     */
    @Override
    public void setCompact(boolean compact) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setBooleanValue(compact);
        }
    }

    /**
     * Sets (as xml) the "compact" attribute
     */
    @Override
    public void xsetCompact(org.apache.xmlbeans.XmlBoolean compact) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(compact);
        }
    }

    /**
     * Unsets the "compact" attribute
     */
    @Override
    public void unsetCompact() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "allDrilled" attribute
     */
    @Override
    public boolean getAllDrilled() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[11]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "allDrilled" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAllDrilled() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[11]);
            }
            return target;
        }
    }

    /**
     * True if has "allDrilled" attribute
     */
    @Override
    public boolean isSetAllDrilled() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "allDrilled" attribute
     */
    @Override
    public void setAllDrilled(boolean allDrilled) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setBooleanValue(allDrilled);
        }
    }

    /**
     * Sets (as xml) the "allDrilled" attribute
     */
    @Override
    public void xsetAllDrilled(org.apache.xmlbeans.XmlBoolean allDrilled) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(allDrilled);
        }
    }

    /**
     * Unsets the "allDrilled" attribute
     */
    @Override
    public void unsetAllDrilled() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "numFmtId" attribute
     */
    @Override
    public long getNumFmtId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "numFmtId" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STNumFmtId xgetNumFmtId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STNumFmtId target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STNumFmtId)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * True if has "numFmtId" attribute
     */
    @Override
    public boolean isSetNumFmtId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "numFmtId" attribute
     */
    @Override
    public void setNumFmtId(long numFmtId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setLongValue(numFmtId);
        }
    }

    /**
     * Sets (as xml) the "numFmtId" attribute
     */
    @Override
    public void xsetNumFmtId(org.openxmlformats.schemas.spreadsheetml.x2006.main.STNumFmtId numFmtId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STNumFmtId target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STNumFmtId)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STNumFmtId)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(numFmtId);
        }
    }

    /**
     * Unsets the "numFmtId" attribute
     */
    @Override
    public void unsetNumFmtId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "outline" attribute
     */
    @Override
    public boolean getOutline() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[13]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "outline" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetOutline() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[13]);
            }
            return target;
        }
    }

    /**
     * True if has "outline" attribute
     */
    @Override
    public boolean isSetOutline() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "outline" attribute
     */
    @Override
    public void setOutline(boolean outline) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setBooleanValue(outline);
        }
    }

    /**
     * Sets (as xml) the "outline" attribute
     */
    @Override
    public void xsetOutline(org.apache.xmlbeans.XmlBoolean outline) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(outline);
        }
    }

    /**
     * Unsets the "outline" attribute
     */
    @Override
    public void unsetOutline() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "subtotalTop" attribute
     */
    @Override
    public boolean getSubtotalTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[14]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "subtotalTop" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSubtotalTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[14]);
            }
            return target;
        }
    }

    /**
     * True if has "subtotalTop" attribute
     */
    @Override
    public boolean isSetSubtotalTop() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "subtotalTop" attribute
     */
    @Override
    public void setSubtotalTop(boolean subtotalTop) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setBooleanValue(subtotalTop);
        }
    }

    /**
     * Sets (as xml) the "subtotalTop" attribute
     */
    @Override
    public void xsetSubtotalTop(org.apache.xmlbeans.XmlBoolean subtotalTop) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(subtotalTop);
        }
    }

    /**
     * Unsets the "subtotalTop" attribute
     */
    @Override
    public void unsetSubtotalTop() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Gets the "dragToRow" attribute
     */
    @Override
    public boolean getDragToRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "dragToRow" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDragToRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return target;
        }
    }

    /**
     * True if has "dragToRow" attribute
     */
    @Override
    public boolean isSetDragToRow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[15]) != null;
        }
    }

    /**
     * Sets the "dragToRow" attribute
     */
    @Override
    public void setDragToRow(boolean dragToRow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setBooleanValue(dragToRow);
        }
    }

    /**
     * Sets (as xml) the "dragToRow" attribute
     */
    @Override
    public void xsetDragToRow(org.apache.xmlbeans.XmlBoolean dragToRow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(dragToRow);
        }
    }

    /**
     * Unsets the "dragToRow" attribute
     */
    @Override
    public void unsetDragToRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Gets the "dragToCol" attribute
     */
    @Override
    public boolean getDragToCol() {
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
     * Gets (as xml) the "dragToCol" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDragToCol() {
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
     * True if has "dragToCol" attribute
     */
    @Override
    public boolean isSetDragToCol() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[16]) != null;
        }
    }

    /**
     * Sets the "dragToCol" attribute
     */
    @Override
    public void setDragToCol(boolean dragToCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.setBooleanValue(dragToCol);
        }
    }

    /**
     * Sets (as xml) the "dragToCol" attribute
     */
    @Override
    public void xsetDragToCol(org.apache.xmlbeans.XmlBoolean dragToCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.set(dragToCol);
        }
    }

    /**
     * Unsets the "dragToCol" attribute
     */
    @Override
    public void unsetDragToCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Gets the "multipleItemSelectionAllowed" attribute
     */
    @Override
    public boolean getMultipleItemSelectionAllowed() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[17]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "multipleItemSelectionAllowed" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetMultipleItemSelectionAllowed() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[17]);
            }
            return target;
        }
    }

    /**
     * True if has "multipleItemSelectionAllowed" attribute
     */
    @Override
    public boolean isSetMultipleItemSelectionAllowed() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[17]) != null;
        }
    }

    /**
     * Sets the "multipleItemSelectionAllowed" attribute
     */
    @Override
    public void setMultipleItemSelectionAllowed(boolean multipleItemSelectionAllowed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.setBooleanValue(multipleItemSelectionAllowed);
        }
    }

    /**
     * Sets (as xml) the "multipleItemSelectionAllowed" attribute
     */
    @Override
    public void xsetMultipleItemSelectionAllowed(org.apache.xmlbeans.XmlBoolean multipleItemSelectionAllowed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.set(multipleItemSelectionAllowed);
        }
    }

    /**
     * Unsets the "multipleItemSelectionAllowed" attribute
     */
    @Override
    public void unsetMultipleItemSelectionAllowed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Gets the "dragToPage" attribute
     */
    @Override
    public boolean getDragToPage() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[18]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "dragToPage" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDragToPage() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[18]);
            }
            return target;
        }
    }

    /**
     * True if has "dragToPage" attribute
     */
    @Override
    public boolean isSetDragToPage() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[18]) != null;
        }
    }

    /**
     * Sets the "dragToPage" attribute
     */
    @Override
    public void setDragToPage(boolean dragToPage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.setBooleanValue(dragToPage);
        }
    }

    /**
     * Sets (as xml) the "dragToPage" attribute
     */
    @Override
    public void xsetDragToPage(org.apache.xmlbeans.XmlBoolean dragToPage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.set(dragToPage);
        }
    }

    /**
     * Unsets the "dragToPage" attribute
     */
    @Override
    public void unsetDragToPage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Gets the "dragToData" attribute
     */
    @Override
    public boolean getDragToData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[19]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "dragToData" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDragToData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[19]);
            }
            return target;
        }
    }

    /**
     * True if has "dragToData" attribute
     */
    @Override
    public boolean isSetDragToData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "dragToData" attribute
     */
    @Override
    public void setDragToData(boolean dragToData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setBooleanValue(dragToData);
        }
    }

    /**
     * Sets (as xml) the "dragToData" attribute
     */
    @Override
    public void xsetDragToData(org.apache.xmlbeans.XmlBoolean dragToData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(dragToData);
        }
    }

    /**
     * Unsets the "dragToData" attribute
     */
    @Override
    public void unsetDragToData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "dragOff" attribute
     */
    @Override
    public boolean getDragOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[20]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "dragOff" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDragOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[20]);
            }
            return target;
        }
    }

    /**
     * True if has "dragOff" attribute
     */
    @Override
    public boolean isSetDragOff() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "dragOff" attribute
     */
    @Override
    public void setDragOff(boolean dragOff) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setBooleanValue(dragOff);
        }
    }

    /**
     * Sets (as xml) the "dragOff" attribute
     */
    @Override
    public void xsetDragOff(org.apache.xmlbeans.XmlBoolean dragOff) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(dragOff);
        }
    }

    /**
     * Unsets the "dragOff" attribute
     */
    @Override
    public void unsetDragOff() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Gets the "showAll" attribute
     */
    @Override
    public boolean getShowAll() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[21]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showAll" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowAll() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[21]);
            }
            return target;
        }
    }

    /**
     * True if has "showAll" attribute
     */
    @Override
    public boolean isSetShowAll() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[21]) != null;
        }
    }

    /**
     * Sets the "showAll" attribute
     */
    @Override
    public void setShowAll(boolean showAll) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.setBooleanValue(showAll);
        }
    }

    /**
     * Sets (as xml) the "showAll" attribute
     */
    @Override
    public void xsetShowAll(org.apache.xmlbeans.XmlBoolean showAll) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.set(showAll);
        }
    }

    /**
     * Unsets the "showAll" attribute
     */
    @Override
    public void unsetShowAll() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Gets the "insertBlankRow" attribute
     */
    @Override
    public boolean getInsertBlankRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[22]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "insertBlankRow" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetInsertBlankRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[22]);
            }
            return target;
        }
    }

    /**
     * True if has "insertBlankRow" attribute
     */
    @Override
    public boolean isSetInsertBlankRow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "insertBlankRow" attribute
     */
    @Override
    public void setInsertBlankRow(boolean insertBlankRow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setBooleanValue(insertBlankRow);
        }
    }

    /**
     * Sets (as xml) the "insertBlankRow" attribute
     */
    @Override
    public void xsetInsertBlankRow(org.apache.xmlbeans.XmlBoolean insertBlankRow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(insertBlankRow);
        }
    }

    /**
     * Unsets the "insertBlankRow" attribute
     */
    @Override
    public void unsetInsertBlankRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Gets the "serverField" attribute
     */
    @Override
    public boolean getServerField() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[23]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "serverField" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetServerField() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[23]);
            }
            return target;
        }
    }

    /**
     * True if has "serverField" attribute
     */
    @Override
    public boolean isSetServerField() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[23]) != null;
        }
    }

    /**
     * Sets the "serverField" attribute
     */
    @Override
    public void setServerField(boolean serverField) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.setBooleanValue(serverField);
        }
    }

    /**
     * Sets (as xml) the "serverField" attribute
     */
    @Override
    public void xsetServerField(org.apache.xmlbeans.XmlBoolean serverField) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.set(serverField);
        }
    }

    /**
     * Unsets the "serverField" attribute
     */
    @Override
    public void unsetServerField() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Gets the "insertPageBreak" attribute
     */
    @Override
    public boolean getInsertPageBreak() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[24]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "insertPageBreak" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetInsertPageBreak() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[24]);
            }
            return target;
        }
    }

    /**
     * True if has "insertPageBreak" attribute
     */
    @Override
    public boolean isSetInsertPageBreak() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[24]) != null;
        }
    }

    /**
     * Sets the "insertPageBreak" attribute
     */
    @Override
    public void setInsertPageBreak(boolean insertPageBreak) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.setBooleanValue(insertPageBreak);
        }
    }

    /**
     * Sets (as xml) the "insertPageBreak" attribute
     */
    @Override
    public void xsetInsertPageBreak(org.apache.xmlbeans.XmlBoolean insertPageBreak) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.set(insertPageBreak);
        }
    }

    /**
     * Unsets the "insertPageBreak" attribute
     */
    @Override
    public void unsetInsertPageBreak() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Gets the "autoShow" attribute
     */
    @Override
    public boolean getAutoShow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[25]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "autoShow" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAutoShow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[25]);
            }
            return target;
        }
    }

    /**
     * True if has "autoShow" attribute
     */
    @Override
    public boolean isSetAutoShow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[25]) != null;
        }
    }

    /**
     * Sets the "autoShow" attribute
     */
    @Override
    public void setAutoShow(boolean autoShow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.setBooleanValue(autoShow);
        }
    }

    /**
     * Sets (as xml) the "autoShow" attribute
     */
    @Override
    public void xsetAutoShow(org.apache.xmlbeans.XmlBoolean autoShow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.set(autoShow);
        }
    }

    /**
     * Unsets the "autoShow" attribute
     */
    @Override
    public void unsetAutoShow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Gets the "topAutoShow" attribute
     */
    @Override
    public boolean getTopAutoShow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[26]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "topAutoShow" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetTopAutoShow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[26]);
            }
            return target;
        }
    }

    /**
     * True if has "topAutoShow" attribute
     */
    @Override
    public boolean isSetTopAutoShow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[26]) != null;
        }
    }

    /**
     * Sets the "topAutoShow" attribute
     */
    @Override
    public void setTopAutoShow(boolean topAutoShow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.setBooleanValue(topAutoShow);
        }
    }

    /**
     * Sets (as xml) the "topAutoShow" attribute
     */
    @Override
    public void xsetTopAutoShow(org.apache.xmlbeans.XmlBoolean topAutoShow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.set(topAutoShow);
        }
    }

    /**
     * Unsets the "topAutoShow" attribute
     */
    @Override
    public void unsetTopAutoShow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Gets the "hideNewItems" attribute
     */
    @Override
    public boolean getHideNewItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[27]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "hideNewItems" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetHideNewItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[27]);
            }
            return target;
        }
    }

    /**
     * True if has "hideNewItems" attribute
     */
    @Override
    public boolean isSetHideNewItems() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[27]) != null;
        }
    }

    /**
     * Sets the "hideNewItems" attribute
     */
    @Override
    public void setHideNewItems(boolean hideNewItems) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.setBooleanValue(hideNewItems);
        }
    }

    /**
     * Sets (as xml) the "hideNewItems" attribute
     */
    @Override
    public void xsetHideNewItems(org.apache.xmlbeans.XmlBoolean hideNewItems) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.set(hideNewItems);
        }
    }

    /**
     * Unsets the "hideNewItems" attribute
     */
    @Override
    public void unsetHideNewItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[27]);
        }
    }

    /**
     * Gets the "measureFilter" attribute
     */
    @Override
    public boolean getMeasureFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[28]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "measureFilter" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetMeasureFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[28]);
            }
            return target;
        }
    }

    /**
     * True if has "measureFilter" attribute
     */
    @Override
    public boolean isSetMeasureFilter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[28]) != null;
        }
    }

    /**
     * Sets the "measureFilter" attribute
     */
    @Override
    public void setMeasureFilter(boolean measureFilter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.setBooleanValue(measureFilter);
        }
    }

    /**
     * Sets (as xml) the "measureFilter" attribute
     */
    @Override
    public void xsetMeasureFilter(org.apache.xmlbeans.XmlBoolean measureFilter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.set(measureFilter);
        }
    }

    /**
     * Unsets the "measureFilter" attribute
     */
    @Override
    public void unsetMeasureFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[28]);
        }
    }

    /**
     * Gets the "includeNewItemsInFilter" attribute
     */
    @Override
    public boolean getIncludeNewItemsInFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[29]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "includeNewItemsInFilter" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetIncludeNewItemsInFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[29]);
            }
            return target;
        }
    }

    /**
     * True if has "includeNewItemsInFilter" attribute
     */
    @Override
    public boolean isSetIncludeNewItemsInFilter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[29]) != null;
        }
    }

    /**
     * Sets the "includeNewItemsInFilter" attribute
     */
    @Override
    public void setIncludeNewItemsInFilter(boolean includeNewItemsInFilter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[29]);
            }
            target.setBooleanValue(includeNewItemsInFilter);
        }
    }

    /**
     * Sets (as xml) the "includeNewItemsInFilter" attribute
     */
    @Override
    public void xsetIncludeNewItemsInFilter(org.apache.xmlbeans.XmlBoolean includeNewItemsInFilter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[29]);
            }
            target.set(includeNewItemsInFilter);
        }
    }

    /**
     * Unsets the "includeNewItemsInFilter" attribute
     */
    @Override
    public void unsetIncludeNewItemsInFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[29]);
        }
    }

    /**
     * Gets the "itemPageCount" attribute
     */
    @Override
    public long getItemPageCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[30]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[30]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "itemPageCount" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetItemPageCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[30]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[30]);
            }
            return target;
        }
    }

    /**
     * True if has "itemPageCount" attribute
     */
    @Override
    public boolean isSetItemPageCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[30]) != null;
        }
    }

    /**
     * Sets the "itemPageCount" attribute
     */
    @Override
    public void setItemPageCount(long itemPageCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[30]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[30]);
            }
            target.setLongValue(itemPageCount);
        }
    }

    /**
     * Sets (as xml) the "itemPageCount" attribute
     */
    @Override
    public void xsetItemPageCount(org.apache.xmlbeans.XmlUnsignedInt itemPageCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[30]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[30]);
            }
            target.set(itemPageCount);
        }
    }

    /**
     * Unsets the "itemPageCount" attribute
     */
    @Override
    public void unsetItemPageCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[30]);
        }
    }

    /**
     * Gets the "sortType" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType.Enum getSortType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[31]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[31]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "sortType" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType xgetSortType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType)get_store().find_attribute_user(PROPERTY_QNAME[31]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType)get_default_attribute_value(PROPERTY_QNAME[31]);
            }
            return target;
        }
    }

    /**
     * True if has "sortType" attribute
     */
    @Override
    public boolean isSetSortType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[31]) != null;
        }
    }

    /**
     * Sets the "sortType" attribute
     */
    @Override
    public void setSortType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType.Enum sortType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[31]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[31]);
            }
            target.setEnumValue(sortType);
        }
    }

    /**
     * Sets (as xml) the "sortType" attribute
     */
    @Override
    public void xsetSortType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType sortType) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType)get_store().find_attribute_user(PROPERTY_QNAME[31]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType)get_store().add_attribute_user(PROPERTY_QNAME[31]);
            }
            target.set(sortType);
        }
    }

    /**
     * Unsets the "sortType" attribute
     */
    @Override
    public void unsetSortType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[31]);
        }
    }

    /**
     * Gets the "dataSourceSort" attribute
     */
    @Override
    public boolean getDataSourceSort() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[32]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "dataSourceSort" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDataSourceSort() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * True if has "dataSourceSort" attribute
     */
    @Override
    public boolean isSetDataSourceSort() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[32]) != null;
        }
    }

    /**
     * Sets the "dataSourceSort" attribute
     */
    @Override
    public void setDataSourceSort(boolean dataSourceSort) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[32]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[32]);
            }
            target.setBooleanValue(dataSourceSort);
        }
    }

    /**
     * Sets (as xml) the "dataSourceSort" attribute
     */
    @Override
    public void xsetDataSourceSort(org.apache.xmlbeans.XmlBoolean dataSourceSort) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[32]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[32]);
            }
            target.set(dataSourceSort);
        }
    }

    /**
     * Unsets the "dataSourceSort" attribute
     */
    @Override
    public void unsetDataSourceSort() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[32]);
        }
    }

    /**
     * Gets the "nonAutoSortDefault" attribute
     */
    @Override
    public boolean getNonAutoSortDefault() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[33]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[33]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "nonAutoSortDefault" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetNonAutoSortDefault() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[33]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[33]);
            }
            return target;
        }
    }

    /**
     * True if has "nonAutoSortDefault" attribute
     */
    @Override
    public boolean isSetNonAutoSortDefault() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[33]) != null;
        }
    }

    /**
     * Sets the "nonAutoSortDefault" attribute
     */
    @Override
    public void setNonAutoSortDefault(boolean nonAutoSortDefault) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[33]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[33]);
            }
            target.setBooleanValue(nonAutoSortDefault);
        }
    }

    /**
     * Sets (as xml) the "nonAutoSortDefault" attribute
     */
    @Override
    public void xsetNonAutoSortDefault(org.apache.xmlbeans.XmlBoolean nonAutoSortDefault) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[33]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[33]);
            }
            target.set(nonAutoSortDefault);
        }
    }

    /**
     * Unsets the "nonAutoSortDefault" attribute
     */
    @Override
    public void unsetNonAutoSortDefault() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[33]);
        }
    }

    /**
     * Gets the "rankBy" attribute
     */
    @Override
    public long getRankBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[34]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "rankBy" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetRankBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[34]);
            return target;
        }
    }

    /**
     * True if has "rankBy" attribute
     */
    @Override
    public boolean isSetRankBy() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[34]) != null;
        }
    }

    /**
     * Sets the "rankBy" attribute
     */
    @Override
    public void setRankBy(long rankBy) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[34]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[34]);
            }
            target.setLongValue(rankBy);
        }
    }

    /**
     * Sets (as xml) the "rankBy" attribute
     */
    @Override
    public void xsetRankBy(org.apache.xmlbeans.XmlUnsignedInt rankBy) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[34]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[34]);
            }
            target.set(rankBy);
        }
    }

    /**
     * Unsets the "rankBy" attribute
     */
    @Override
    public void unsetRankBy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[34]);
        }
    }

    /**
     * Gets the "defaultSubtotal" attribute
     */
    @Override
    public boolean getDefaultSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[35]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "defaultSubtotal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDefaultSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[35]);
            }
            return target;
        }
    }

    /**
     * True if has "defaultSubtotal" attribute
     */
    @Override
    public boolean isSetDefaultSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[35]) != null;
        }
    }

    /**
     * Sets the "defaultSubtotal" attribute
     */
    @Override
    public void setDefaultSubtotal(boolean defaultSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[35]);
            }
            target.setBooleanValue(defaultSubtotal);
        }
    }

    /**
     * Sets (as xml) the "defaultSubtotal" attribute
     */
    @Override
    public void xsetDefaultSubtotal(org.apache.xmlbeans.XmlBoolean defaultSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[35]);
            }
            target.set(defaultSubtotal);
        }
    }

    /**
     * Unsets the "defaultSubtotal" attribute
     */
    @Override
    public void unsetDefaultSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[35]);
        }
    }

    /**
     * Gets the "sumSubtotal" attribute
     */
    @Override
    public boolean getSumSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[36]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "sumSubtotal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSumSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[36]);
            }
            return target;
        }
    }

    /**
     * True if has "sumSubtotal" attribute
     */
    @Override
    public boolean isSetSumSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[36]) != null;
        }
    }

    /**
     * Sets the "sumSubtotal" attribute
     */
    @Override
    public void setSumSubtotal(boolean sumSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[36]);
            }
            target.setBooleanValue(sumSubtotal);
        }
    }

    /**
     * Sets (as xml) the "sumSubtotal" attribute
     */
    @Override
    public void xsetSumSubtotal(org.apache.xmlbeans.XmlBoolean sumSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[36]);
            }
            target.set(sumSubtotal);
        }
    }

    /**
     * Unsets the "sumSubtotal" attribute
     */
    @Override
    public void unsetSumSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[36]);
        }
    }

    /**
     * Gets the "countASubtotal" attribute
     */
    @Override
    public boolean getCountASubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[37]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "countASubtotal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCountASubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[37]);
            }
            return target;
        }
    }

    /**
     * True if has "countASubtotal" attribute
     */
    @Override
    public boolean isSetCountASubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[37]) != null;
        }
    }

    /**
     * Sets the "countASubtotal" attribute
     */
    @Override
    public void setCountASubtotal(boolean countASubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[37]);
            }
            target.setBooleanValue(countASubtotal);
        }
    }

    /**
     * Sets (as xml) the "countASubtotal" attribute
     */
    @Override
    public void xsetCountASubtotal(org.apache.xmlbeans.XmlBoolean countASubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[37]);
            }
            target.set(countASubtotal);
        }
    }

    /**
     * Unsets the "countASubtotal" attribute
     */
    @Override
    public void unsetCountASubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[37]);
        }
    }

    /**
     * Gets the "avgSubtotal" attribute
     */
    @Override
    public boolean getAvgSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[38]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "avgSubtotal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAvgSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[38]);
            }
            return target;
        }
    }

    /**
     * True if has "avgSubtotal" attribute
     */
    @Override
    public boolean isSetAvgSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[38]) != null;
        }
    }

    /**
     * Sets the "avgSubtotal" attribute
     */
    @Override
    public void setAvgSubtotal(boolean avgSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[38]);
            }
            target.setBooleanValue(avgSubtotal);
        }
    }

    /**
     * Sets (as xml) the "avgSubtotal" attribute
     */
    @Override
    public void xsetAvgSubtotal(org.apache.xmlbeans.XmlBoolean avgSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[38]);
            }
            target.set(avgSubtotal);
        }
    }

    /**
     * Unsets the "avgSubtotal" attribute
     */
    @Override
    public void unsetAvgSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[38]);
        }
    }

    /**
     * Gets the "maxSubtotal" attribute
     */
    @Override
    public boolean getMaxSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[39]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "maxSubtotal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetMaxSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[39]);
            }
            return target;
        }
    }

    /**
     * True if has "maxSubtotal" attribute
     */
    @Override
    public boolean isSetMaxSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[39]) != null;
        }
    }

    /**
     * Sets the "maxSubtotal" attribute
     */
    @Override
    public void setMaxSubtotal(boolean maxSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[39]);
            }
            target.setBooleanValue(maxSubtotal);
        }
    }

    /**
     * Sets (as xml) the "maxSubtotal" attribute
     */
    @Override
    public void xsetMaxSubtotal(org.apache.xmlbeans.XmlBoolean maxSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[39]);
            }
            target.set(maxSubtotal);
        }
    }

    /**
     * Unsets the "maxSubtotal" attribute
     */
    @Override
    public void unsetMaxSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[39]);
        }
    }

    /**
     * Gets the "minSubtotal" attribute
     */
    @Override
    public boolean getMinSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[40]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[40]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "minSubtotal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetMinSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[40]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[40]);
            }
            return target;
        }
    }

    /**
     * True if has "minSubtotal" attribute
     */
    @Override
    public boolean isSetMinSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[40]) != null;
        }
    }

    /**
     * Sets the "minSubtotal" attribute
     */
    @Override
    public void setMinSubtotal(boolean minSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[40]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[40]);
            }
            target.setBooleanValue(minSubtotal);
        }
    }

    /**
     * Sets (as xml) the "minSubtotal" attribute
     */
    @Override
    public void xsetMinSubtotal(org.apache.xmlbeans.XmlBoolean minSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[40]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[40]);
            }
            target.set(minSubtotal);
        }
    }

    /**
     * Unsets the "minSubtotal" attribute
     */
    @Override
    public void unsetMinSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[40]);
        }
    }

    /**
     * Gets the "productSubtotal" attribute
     */
    @Override
    public boolean getProductSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[41]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[41]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "productSubtotal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetProductSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[41]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[41]);
            }
            return target;
        }
    }

    /**
     * True if has "productSubtotal" attribute
     */
    @Override
    public boolean isSetProductSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[41]) != null;
        }
    }

    /**
     * Sets the "productSubtotal" attribute
     */
    @Override
    public void setProductSubtotal(boolean productSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[41]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[41]);
            }
            target.setBooleanValue(productSubtotal);
        }
    }

    /**
     * Sets (as xml) the "productSubtotal" attribute
     */
    @Override
    public void xsetProductSubtotal(org.apache.xmlbeans.XmlBoolean productSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[41]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[41]);
            }
            target.set(productSubtotal);
        }
    }

    /**
     * Unsets the "productSubtotal" attribute
     */
    @Override
    public void unsetProductSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[41]);
        }
    }

    /**
     * Gets the "countSubtotal" attribute
     */
    @Override
    public boolean getCountSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[42]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[42]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "countSubtotal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCountSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[42]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[42]);
            }
            return target;
        }
    }

    /**
     * True if has "countSubtotal" attribute
     */
    @Override
    public boolean isSetCountSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[42]) != null;
        }
    }

    /**
     * Sets the "countSubtotal" attribute
     */
    @Override
    public void setCountSubtotal(boolean countSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[42]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[42]);
            }
            target.setBooleanValue(countSubtotal);
        }
    }

    /**
     * Sets (as xml) the "countSubtotal" attribute
     */
    @Override
    public void xsetCountSubtotal(org.apache.xmlbeans.XmlBoolean countSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[42]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[42]);
            }
            target.set(countSubtotal);
        }
    }

    /**
     * Unsets the "countSubtotal" attribute
     */
    @Override
    public void unsetCountSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[42]);
        }
    }

    /**
     * Gets the "stdDevSubtotal" attribute
     */
    @Override
    public boolean getStdDevSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[43]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[43]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "stdDevSubtotal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetStdDevSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[43]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[43]);
            }
            return target;
        }
    }

    /**
     * True if has "stdDevSubtotal" attribute
     */
    @Override
    public boolean isSetStdDevSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[43]) != null;
        }
    }

    /**
     * Sets the "stdDevSubtotal" attribute
     */
    @Override
    public void setStdDevSubtotal(boolean stdDevSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[43]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[43]);
            }
            target.setBooleanValue(stdDevSubtotal);
        }
    }

    /**
     * Sets (as xml) the "stdDevSubtotal" attribute
     */
    @Override
    public void xsetStdDevSubtotal(org.apache.xmlbeans.XmlBoolean stdDevSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[43]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[43]);
            }
            target.set(stdDevSubtotal);
        }
    }

    /**
     * Unsets the "stdDevSubtotal" attribute
     */
    @Override
    public void unsetStdDevSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[43]);
        }
    }

    /**
     * Gets the "stdDevPSubtotal" attribute
     */
    @Override
    public boolean getStdDevPSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[44]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[44]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "stdDevPSubtotal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetStdDevPSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[44]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[44]);
            }
            return target;
        }
    }

    /**
     * True if has "stdDevPSubtotal" attribute
     */
    @Override
    public boolean isSetStdDevPSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[44]) != null;
        }
    }

    /**
     * Sets the "stdDevPSubtotal" attribute
     */
    @Override
    public void setStdDevPSubtotal(boolean stdDevPSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[44]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[44]);
            }
            target.setBooleanValue(stdDevPSubtotal);
        }
    }

    /**
     * Sets (as xml) the "stdDevPSubtotal" attribute
     */
    @Override
    public void xsetStdDevPSubtotal(org.apache.xmlbeans.XmlBoolean stdDevPSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[44]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[44]);
            }
            target.set(stdDevPSubtotal);
        }
    }

    /**
     * Unsets the "stdDevPSubtotal" attribute
     */
    @Override
    public void unsetStdDevPSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[44]);
        }
    }

    /**
     * Gets the "varSubtotal" attribute
     */
    @Override
    public boolean getVarSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[45]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[45]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "varSubtotal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetVarSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[45]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[45]);
            }
            return target;
        }
    }

    /**
     * True if has "varSubtotal" attribute
     */
    @Override
    public boolean isSetVarSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[45]) != null;
        }
    }

    /**
     * Sets the "varSubtotal" attribute
     */
    @Override
    public void setVarSubtotal(boolean varSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[45]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[45]);
            }
            target.setBooleanValue(varSubtotal);
        }
    }

    /**
     * Sets (as xml) the "varSubtotal" attribute
     */
    @Override
    public void xsetVarSubtotal(org.apache.xmlbeans.XmlBoolean varSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[45]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[45]);
            }
            target.set(varSubtotal);
        }
    }

    /**
     * Unsets the "varSubtotal" attribute
     */
    @Override
    public void unsetVarSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[45]);
        }
    }

    /**
     * Gets the "varPSubtotal" attribute
     */
    @Override
    public boolean getVarPSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[46]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[46]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "varPSubtotal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetVarPSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[46]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[46]);
            }
            return target;
        }
    }

    /**
     * True if has "varPSubtotal" attribute
     */
    @Override
    public boolean isSetVarPSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[46]) != null;
        }
    }

    /**
     * Sets the "varPSubtotal" attribute
     */
    @Override
    public void setVarPSubtotal(boolean varPSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[46]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[46]);
            }
            target.setBooleanValue(varPSubtotal);
        }
    }

    /**
     * Sets (as xml) the "varPSubtotal" attribute
     */
    @Override
    public void xsetVarPSubtotal(org.apache.xmlbeans.XmlBoolean varPSubtotal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[46]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[46]);
            }
            target.set(varPSubtotal);
        }
    }

    /**
     * Unsets the "varPSubtotal" attribute
     */
    @Override
    public void unsetVarPSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[46]);
        }
    }

    /**
     * Gets the "showPropCell" attribute
     */
    @Override
    public boolean getShowPropCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[47]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[47]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showPropCell" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowPropCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[47]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[47]);
            }
            return target;
        }
    }

    /**
     * True if has "showPropCell" attribute
     */
    @Override
    public boolean isSetShowPropCell() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[47]) != null;
        }
    }

    /**
     * Sets the "showPropCell" attribute
     */
    @Override
    public void setShowPropCell(boolean showPropCell) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[47]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[47]);
            }
            target.setBooleanValue(showPropCell);
        }
    }

    /**
     * Sets (as xml) the "showPropCell" attribute
     */
    @Override
    public void xsetShowPropCell(org.apache.xmlbeans.XmlBoolean showPropCell) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[47]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[47]);
            }
            target.set(showPropCell);
        }
    }

    /**
     * Unsets the "showPropCell" attribute
     */
    @Override
    public void unsetShowPropCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[47]);
        }
    }

    /**
     * Gets the "showPropTip" attribute
     */
    @Override
    public boolean getShowPropTip() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[48]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[48]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showPropTip" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowPropTip() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[48]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[48]);
            }
            return target;
        }
    }

    /**
     * True if has "showPropTip" attribute
     */
    @Override
    public boolean isSetShowPropTip() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[48]) != null;
        }
    }

    /**
     * Sets the "showPropTip" attribute
     */
    @Override
    public void setShowPropTip(boolean showPropTip) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[48]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[48]);
            }
            target.setBooleanValue(showPropTip);
        }
    }

    /**
     * Sets (as xml) the "showPropTip" attribute
     */
    @Override
    public void xsetShowPropTip(org.apache.xmlbeans.XmlBoolean showPropTip) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[48]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[48]);
            }
            target.set(showPropTip);
        }
    }

    /**
     * Unsets the "showPropTip" attribute
     */
    @Override
    public void unsetShowPropTip() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[48]);
        }
    }

    /**
     * Gets the "showPropAsCaption" attribute
     */
    @Override
    public boolean getShowPropAsCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[49]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[49]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showPropAsCaption" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowPropAsCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[49]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[49]);
            }
            return target;
        }
    }

    /**
     * True if has "showPropAsCaption" attribute
     */
    @Override
    public boolean isSetShowPropAsCaption() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[49]) != null;
        }
    }

    /**
     * Sets the "showPropAsCaption" attribute
     */
    @Override
    public void setShowPropAsCaption(boolean showPropAsCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[49]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[49]);
            }
            target.setBooleanValue(showPropAsCaption);
        }
    }

    /**
     * Sets (as xml) the "showPropAsCaption" attribute
     */
    @Override
    public void xsetShowPropAsCaption(org.apache.xmlbeans.XmlBoolean showPropAsCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[49]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[49]);
            }
            target.set(showPropAsCaption);
        }
    }

    /**
     * Unsets the "showPropAsCaption" attribute
     */
    @Override
    public void unsetShowPropAsCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[49]);
        }
    }

    /**
     * Gets the "defaultAttributeDrillState" attribute
     */
    @Override
    public boolean getDefaultAttributeDrillState() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[50]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[50]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "defaultAttributeDrillState" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDefaultAttributeDrillState() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[50]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[50]);
            }
            return target;
        }
    }

    /**
     * True if has "defaultAttributeDrillState" attribute
     */
    @Override
    public boolean isSetDefaultAttributeDrillState() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[50]) != null;
        }
    }

    /**
     * Sets the "defaultAttributeDrillState" attribute
     */
    @Override
    public void setDefaultAttributeDrillState(boolean defaultAttributeDrillState) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[50]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[50]);
            }
            target.setBooleanValue(defaultAttributeDrillState);
        }
    }

    /**
     * Sets (as xml) the "defaultAttributeDrillState" attribute
     */
    @Override
    public void xsetDefaultAttributeDrillState(org.apache.xmlbeans.XmlBoolean defaultAttributeDrillState) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[50]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[50]);
            }
            target.set(defaultAttributeDrillState);
        }
    }

    /**
     * Unsets the "defaultAttributeDrillState" attribute
     */
    @Override
    public void unsetDefaultAttributeDrillState() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[50]);
        }
    }
}
