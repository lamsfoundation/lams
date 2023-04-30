/*
 * XML Type:  CT_Selection
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Selection(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTSelectionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection {
    private static final long serialVersionUID = 1L;

    public CTSelectionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "pane"),
        new QName("", "activeCell"),
        new QName("", "activeCellId"),
        new QName("", "sqref"),
    };


    /**
     * Gets the "pane" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.Enum getPane() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "pane" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane xgetPane() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "pane" attribute
     */
    @Override
    public boolean isSetPane() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "pane" attribute
     */
    @Override
    public void setPane(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.Enum pane) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(pane);
        }
    }

    /**
     * Sets (as xml) the "pane" attribute
     */
    @Override
    public void xsetPane(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane pane) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(pane);
        }
    }

    /**
     * Unsets the "pane" attribute
     */
    @Override
    public void unsetPane() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "activeCell" attribute
     */
    @Override
    public java.lang.String getActiveCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "activeCell" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef xgetActiveCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "activeCell" attribute
     */
    @Override
    public boolean isSetActiveCell() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "activeCell" attribute
     */
    @Override
    public void setActiveCell(java.lang.String activeCell) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(activeCell);
        }
    }

    /**
     * Sets (as xml) the "activeCell" attribute
     */
    @Override
    public void xsetActiveCell(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef activeCell) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(activeCell);
        }
    }

    /**
     * Unsets the "activeCell" attribute
     */
    @Override
    public void unsetActiveCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "activeCellId" attribute
     */
    @Override
    public long getActiveCellId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "activeCellId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetActiveCellId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "activeCellId" attribute
     */
    @Override
    public boolean isSetActiveCellId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "activeCellId" attribute
     */
    @Override
    public void setActiveCellId(long activeCellId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setLongValue(activeCellId);
        }
    }

    /**
     * Sets (as xml) the "activeCellId" attribute
     */
    @Override
    public void xsetActiveCellId(org.apache.xmlbeans.XmlUnsignedInt activeCellId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(activeCellId);
        }
    }

    /**
     * Unsets the "activeCellId" attribute
     */
    @Override
    public void unsetActiveCellId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "sqref" attribute
     */
    @Override
    public java.util.List getSqref() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? null : target.getListValue();
        }
    }

    /**
     * Gets (as xml) the "sqref" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref xgetSqref() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "sqref" attribute
     */
    @Override
    public boolean isSetSqref() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "sqref" attribute
     */
    @Override
    public void setSqref(java.util.List sqref) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setListValue(sqref);
        }
    }

    /**
     * Sets (as xml) the "sqref" attribute
     */
    @Override
    public void xsetSqref(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref sqref) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(sqref);
        }
    }

    /**
     * Unsets the "sqref" attribute
     */
    @Override
    public void unsetSqref() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}
