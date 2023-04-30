/*
 * XML Type:  CT_DateGroupItem
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DateGroupItem(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTDateGroupItemImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem {
    private static final long serialVersionUID = 1L;

    public CTDateGroupItemImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "year"),
        new QName("", "month"),
        new QName("", "day"),
        new QName("", "hour"),
        new QName("", "minute"),
        new QName("", "second"),
        new QName("", "dateTimeGrouping"),
    };


    /**
     * Gets the "year" attribute
     */
    @Override
    public int getYear() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "year" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedShort xgetYear() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "year" attribute
     */
    @Override
    public void setYear(int year) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setIntValue(year);
        }
    }

    /**
     * Sets (as xml) the "year" attribute
     */
    @Override
    public void xsetYear(org.apache.xmlbeans.XmlUnsignedShort year) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(year);
        }
    }

    /**
     * Gets the "month" attribute
     */
    @Override
    public int getMonth() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "month" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedShort xgetMonth() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "month" attribute
     */
    @Override
    public boolean isSetMonth() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "month" attribute
     */
    @Override
    public void setMonth(int month) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setIntValue(month);
        }
    }

    /**
     * Sets (as xml) the "month" attribute
     */
    @Override
    public void xsetMonth(org.apache.xmlbeans.XmlUnsignedShort month) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(month);
        }
    }

    /**
     * Unsets the "month" attribute
     */
    @Override
    public void unsetMonth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "day" attribute
     */
    @Override
    public int getDay() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "day" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedShort xgetDay() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "day" attribute
     */
    @Override
    public boolean isSetDay() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "day" attribute
     */
    @Override
    public void setDay(int day) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setIntValue(day);
        }
    }

    /**
     * Sets (as xml) the "day" attribute
     */
    @Override
    public void xsetDay(org.apache.xmlbeans.XmlUnsignedShort day) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(day);
        }
    }

    /**
     * Unsets the "day" attribute
     */
    @Override
    public void unsetDay() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "hour" attribute
     */
    @Override
    public int getHour() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "hour" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedShort xgetHour() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "hour" attribute
     */
    @Override
    public boolean isSetHour() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "hour" attribute
     */
    @Override
    public void setHour(int hour) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setIntValue(hour);
        }
    }

    /**
     * Sets (as xml) the "hour" attribute
     */
    @Override
    public void xsetHour(org.apache.xmlbeans.XmlUnsignedShort hour) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(hour);
        }
    }

    /**
     * Unsets the "hour" attribute
     */
    @Override
    public void unsetHour() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "minute" attribute
     */
    @Override
    public int getMinute() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "minute" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedShort xgetMinute() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "minute" attribute
     */
    @Override
    public boolean isSetMinute() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "minute" attribute
     */
    @Override
    public void setMinute(int minute) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setIntValue(minute);
        }
    }

    /**
     * Sets (as xml) the "minute" attribute
     */
    @Override
    public void xsetMinute(org.apache.xmlbeans.XmlUnsignedShort minute) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(minute);
        }
    }

    /**
     * Unsets the "minute" attribute
     */
    @Override
    public void unsetMinute() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "second" attribute
     */
    @Override
    public int getSecond() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "second" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedShort xgetSecond() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "second" attribute
     */
    @Override
    public boolean isSetSecond() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "second" attribute
     */
    @Override
    public void setSecond(int second) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setIntValue(second);
        }
    }

    /**
     * Sets (as xml) the "second" attribute
     */
    @Override
    public void xsetSecond(org.apache.xmlbeans.XmlUnsignedShort second) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(second);
        }
    }

    /**
     * Unsets the "second" attribute
     */
    @Override
    public void unsetSecond() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "dateTimeGrouping" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping.Enum getDateTimeGrouping() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "dateTimeGrouping" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping xgetDateTimeGrouping() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Sets the "dateTimeGrouping" attribute
     */
    @Override
    public void setDateTimeGrouping(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping.Enum dateTimeGrouping) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(dateTimeGrouping);
        }
    }

    /**
     * Sets (as xml) the "dateTimeGrouping" attribute
     */
    @Override
    public void xsetDateTimeGrouping(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping dateTimeGrouping) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(dateTimeGrouping);
        }
    }
}
