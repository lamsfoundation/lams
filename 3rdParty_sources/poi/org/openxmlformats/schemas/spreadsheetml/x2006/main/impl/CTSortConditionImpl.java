/*
 * XML Type:  CT_SortCondition
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSortCondition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SortCondition(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTSortConditionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSortCondition {
    private static final long serialVersionUID = 1L;

    public CTSortConditionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "descending"),
        new QName("", "sortBy"),
        new QName("", "ref"),
        new QName("", "customList"),
        new QName("", "dxfId"),
        new QName("", "iconSet"),
        new QName("", "iconId"),
    };


    /**
     * Gets the "descending" attribute
     */
    @Override
    public boolean getDescending() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "descending" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDescending() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "descending" attribute
     */
    @Override
    public boolean isSetDescending() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "descending" attribute
     */
    @Override
    public void setDescending(boolean descending) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setBooleanValue(descending);
        }
    }

    /**
     * Sets (as xml) the "descending" attribute
     */
    @Override
    public void xsetDescending(org.apache.xmlbeans.XmlBoolean descending) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(descending);
        }
    }

    /**
     * Unsets the "descending" attribute
     */
    @Override
    public void unsetDescending() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "sortBy" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortBy.Enum getSortBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortBy.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "sortBy" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortBy xgetSortBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortBy target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortBy)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortBy)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "sortBy" attribute
     */
    @Override
    public boolean isSetSortBy() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "sortBy" attribute
     */
    @Override
    public void setSortBy(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortBy.Enum sortBy) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(sortBy);
        }
    }

    /**
     * Sets (as xml) the "sortBy" attribute
     */
    @Override
    public void xsetSortBy(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortBy sortBy) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortBy target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortBy)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortBy)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(sortBy);
        }
    }

    /**
     * Unsets the "sortBy" attribute
     */
    @Override
    public void unsetSortBy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "ref" attribute
     */
    @Override
    public java.lang.String getRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "ref" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef xgetRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "ref" attribute
     */
    @Override
    public void setRef(java.lang.String ref) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(ref);
        }
    }

    /**
     * Sets (as xml) the "ref" attribute
     */
    @Override
    public void xsetRef(org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef ref) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(ref);
        }
    }

    /**
     * Gets the "customList" attribute
     */
    @Override
    public java.lang.String getCustomList() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "customList" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetCustomList() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "customList" attribute
     */
    @Override
    public boolean isSetCustomList() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "customList" attribute
     */
    @Override
    public void setCustomList(java.lang.String customList) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(customList);
        }
    }

    /**
     * Sets (as xml) the "customList" attribute
     */
    @Override
    public void xsetCustomList(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring customList) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(customList);
        }
    }

    /**
     * Unsets the "customList" attribute
     */
    @Override
    public void unsetCustomList() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "dxfId" attribute
     */
    @Override
    public long getDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "dxfId" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STDxfId xgetDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STDxfId target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDxfId)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "dxfId" attribute
     */
    @Override
    public boolean isSetDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "dxfId" attribute
     */
    @Override
    public void setDxfId(long dxfId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setLongValue(dxfId);
        }
    }

    /**
     * Sets (as xml) the "dxfId" attribute
     */
    @Override
    public void xsetDxfId(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDxfId dxfId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STDxfId target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDxfId)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDxfId)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(dxfId);
        }
    }

    /**
     * Unsets the "dxfId" attribute
     */
    @Override
    public void unsetDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "iconSet" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.Enum getIconSet() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "iconSet" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType xgetIconSet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "iconSet" attribute
     */
    @Override
    public boolean isSetIconSet() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "iconSet" attribute
     */
    @Override
    public void setIconSet(org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.Enum iconSet) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(iconSet);
        }
    }

    /**
     * Sets (as xml) the "iconSet" attribute
     */
    @Override
    public void xsetIconSet(org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType iconSet) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(iconSet);
        }
    }

    /**
     * Unsets the "iconSet" attribute
     */
    @Override
    public void unsetIconSet() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "iconId" attribute
     */
    @Override
    public long getIconId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "iconId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetIconId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "iconId" attribute
     */
    @Override
    public boolean isSetIconId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "iconId" attribute
     */
    @Override
    public void setIconId(long iconId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setLongValue(iconId);
        }
    }

    /**
     * Sets (as xml) the "iconId" attribute
     */
    @Override
    public void xsetIconId(org.apache.xmlbeans.XmlUnsignedInt iconId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(iconId);
        }
    }

    /**
     * Unsets the "iconId" attribute
     */
    @Override
    public void unsetIconId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }
}
