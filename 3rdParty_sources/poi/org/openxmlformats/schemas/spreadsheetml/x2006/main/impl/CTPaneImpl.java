/*
 * XML Type:  CT_Pane
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Pane(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTPaneImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane {
    private static final long serialVersionUID = 1L;

    public CTPaneImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "xSplit"),
        new QName("", "ySplit"),
        new QName("", "topLeftCell"),
        new QName("", "activePane"),
        new QName("", "state"),
    };


    /**
     * Gets the "xSplit" attribute
     */
    @Override
    public double getXSplit() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return (target == null) ? 0.0 : target.getDoubleValue();
        }
    }

    /**
     * Gets (as xml) the "xSplit" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlDouble xgetXSplit() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDouble)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "xSplit" attribute
     */
    @Override
    public boolean isSetXSplit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "xSplit" attribute
     */
    @Override
    public void setXSplit(double xSplit) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setDoubleValue(xSplit);
        }
    }

    /**
     * Sets (as xml) the "xSplit" attribute
     */
    @Override
    public void xsetXSplit(org.apache.xmlbeans.XmlDouble xSplit) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(xSplit);
        }
    }

    /**
     * Unsets the "xSplit" attribute
     */
    @Override
    public void unsetXSplit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "ySplit" attribute
     */
    @Override
    public double getYSplit() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? 0.0 : target.getDoubleValue();
        }
    }

    /**
     * Gets (as xml) the "ySplit" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlDouble xgetYSplit() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDouble)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "ySplit" attribute
     */
    @Override
    public boolean isSetYSplit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "ySplit" attribute
     */
    @Override
    public void setYSplit(double ySplit) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setDoubleValue(ySplit);
        }
    }

    /**
     * Sets (as xml) the "ySplit" attribute
     */
    @Override
    public void xsetYSplit(org.apache.xmlbeans.XmlDouble ySplit) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(ySplit);
        }
    }

    /**
     * Unsets the "ySplit" attribute
     */
    @Override
    public void unsetYSplit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "topLeftCell" attribute
     */
    @Override
    public java.lang.String getTopLeftCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "topLeftCell" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef xgetTopLeftCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "topLeftCell" attribute
     */
    @Override
    public boolean isSetTopLeftCell() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "topLeftCell" attribute
     */
    @Override
    public void setTopLeftCell(java.lang.String topLeftCell) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(topLeftCell);
        }
    }

    /**
     * Sets (as xml) the "topLeftCell" attribute
     */
    @Override
    public void xsetTopLeftCell(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef topLeftCell) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(topLeftCell);
        }
    }

    /**
     * Unsets the "topLeftCell" attribute
     */
    @Override
    public void unsetTopLeftCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "activePane" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.Enum getActivePane() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "activePane" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane xgetActivePane() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "activePane" attribute
     */
    @Override
    public boolean isSetActivePane() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "activePane" attribute
     */
    @Override
    public void setActivePane(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.Enum activePane) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(activePane);
        }
    }

    /**
     * Sets (as xml) the "activePane" attribute
     */
    @Override
    public void xsetActivePane(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane activePane) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(activePane);
        }
    }

    /**
     * Unsets the "activePane" attribute
     */
    @Override
    public void unsetActivePane() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "state" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState.Enum getState() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "state" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState xgetState() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "state" attribute
     */
    @Override
    public boolean isSetState() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "state" attribute
     */
    @Override
    public void setState(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState.Enum state) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(state);
        }
    }

    /**
     * Sets (as xml) the "state" attribute
     */
    @Override
    public void xsetState(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState state) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(state);
        }
    }

    /**
     * Unsets the "state" attribute
     */
    @Override
    public void unsetState() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}
