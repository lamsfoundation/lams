/*
 * XML Type:  CT_IconFilter
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconFilter
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_IconFilter(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTIconFilterImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconFilter {
    private static final long serialVersionUID = 1L;

    public CTIconFilterImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "iconSet"),
        new QName("", "iconId"),
    };


    /**
     * Gets the "iconSet" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.Enum getIconSet() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(iconSet);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
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
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
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
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
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
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}
