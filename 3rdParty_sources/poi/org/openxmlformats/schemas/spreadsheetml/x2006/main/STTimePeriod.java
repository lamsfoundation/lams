/*
 * XML Type:  ST_TimePeriod
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STTimePeriod
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TimePeriod(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STTimePeriod.
 */
public interface STTimePeriod extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STTimePeriod> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttimeperiodef07type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum TODAY = Enum.forString("today");
    Enum YESTERDAY = Enum.forString("yesterday");
    Enum TOMORROW = Enum.forString("tomorrow");
    Enum LAST_7_DAYS = Enum.forString("last7Days");
    Enum THIS_MONTH = Enum.forString("thisMonth");
    Enum LAST_MONTH = Enum.forString("lastMonth");
    Enum NEXT_MONTH = Enum.forString("nextMonth");
    Enum THIS_WEEK = Enum.forString("thisWeek");
    Enum LAST_WEEK = Enum.forString("lastWeek");
    Enum NEXT_WEEK = Enum.forString("nextWeek");

    int INT_TODAY = Enum.INT_TODAY;
    int INT_YESTERDAY = Enum.INT_YESTERDAY;
    int INT_TOMORROW = Enum.INT_TOMORROW;
    int INT_LAST_7_DAYS = Enum.INT_LAST_7_DAYS;
    int INT_THIS_MONTH = Enum.INT_THIS_MONTH;
    int INT_LAST_MONTH = Enum.INT_LAST_MONTH;
    int INT_NEXT_MONTH = Enum.INT_NEXT_MONTH;
    int INT_THIS_WEEK = Enum.INT_THIS_WEEK;
    int INT_LAST_WEEK = Enum.INT_LAST_WEEK;
    int INT_NEXT_WEEK = Enum.INT_NEXT_WEEK;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STTimePeriod.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_TODAY
     * Enum.forString(s); // returns the enum value for a string
     * Enum.forInt(i); // returns the enum value for an int
     * </pre>
     * Enumeration objects are immutable singleton objects that
     * can be compared using == object equality. They have no
     * public constructor. See the constants defined within this
     * class for all the valid values.
     */
    final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase {
        /**
         * Returns the enum value for a string, or null if none.
         */
        public static Enum forString(java.lang.String s) {
            return (Enum)table.forString(s);
        }

        /**
         * Returns the enum value corresponding to an int, or null if none.
         */
        public static Enum forInt(int i) {
            return (Enum)table.forInt(i);
        }

        private Enum(java.lang.String s, int i) {
            super(s, i);
        }

        static final int INT_TODAY = 1;
        static final int INT_YESTERDAY = 2;
        static final int INT_TOMORROW = 3;
        static final int INT_LAST_7_DAYS = 4;
        static final int INT_THIS_MONTH = 5;
        static final int INT_LAST_MONTH = 6;
        static final int INT_NEXT_MONTH = 7;
        static final int INT_THIS_WEEK = 8;
        static final int INT_LAST_WEEK = 9;
        static final int INT_NEXT_WEEK = 10;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("today", INT_TODAY),
            new Enum("yesterday", INT_YESTERDAY),
            new Enum("tomorrow", INT_TOMORROW),
            new Enum("last7Days", INT_LAST_7_DAYS),
            new Enum("thisMonth", INT_THIS_MONTH),
            new Enum("lastMonth", INT_LAST_MONTH),
            new Enum("nextMonth", INT_NEXT_MONTH),
            new Enum("thisWeek", INT_THIS_WEEK),
            new Enum("lastWeek", INT_LAST_WEEK),
            new Enum("nextWeek", INT_NEXT_WEEK),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
