/*
 * XML Type:  ST_DynamicFilterType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STDynamicFilterType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_DynamicFilterType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STDynamicFilterType.
 */
public interface STDynamicFilterType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STDynamicFilterType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stdynamicfiltertypebbc0type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NULL = Enum.forString("null");
    Enum ABOVE_AVERAGE = Enum.forString("aboveAverage");
    Enum BELOW_AVERAGE = Enum.forString("belowAverage");
    Enum TOMORROW = Enum.forString("tomorrow");
    Enum TODAY = Enum.forString("today");
    Enum YESTERDAY = Enum.forString("yesterday");
    Enum NEXT_WEEK = Enum.forString("nextWeek");
    Enum THIS_WEEK = Enum.forString("thisWeek");
    Enum LAST_WEEK = Enum.forString("lastWeek");
    Enum NEXT_MONTH = Enum.forString("nextMonth");
    Enum THIS_MONTH = Enum.forString("thisMonth");
    Enum LAST_MONTH = Enum.forString("lastMonth");
    Enum NEXT_QUARTER = Enum.forString("nextQuarter");
    Enum THIS_QUARTER = Enum.forString("thisQuarter");
    Enum LAST_QUARTER = Enum.forString("lastQuarter");
    Enum NEXT_YEAR = Enum.forString("nextYear");
    Enum THIS_YEAR = Enum.forString("thisYear");
    Enum LAST_YEAR = Enum.forString("lastYear");
    Enum YEAR_TO_DATE = Enum.forString("yearToDate");
    Enum Q_1 = Enum.forString("Q1");
    Enum Q_2 = Enum.forString("Q2");
    Enum Q_3 = Enum.forString("Q3");
    Enum Q_4 = Enum.forString("Q4");
    Enum M_1 = Enum.forString("M1");
    Enum M_2 = Enum.forString("M2");
    Enum M_3 = Enum.forString("M3");
    Enum M_4 = Enum.forString("M4");
    Enum M_5 = Enum.forString("M5");
    Enum M_6 = Enum.forString("M6");
    Enum M_7 = Enum.forString("M7");
    Enum M_8 = Enum.forString("M8");
    Enum M_9 = Enum.forString("M9");
    Enum M_10 = Enum.forString("M10");
    Enum M_11 = Enum.forString("M11");
    Enum M_12 = Enum.forString("M12");

    int INT_NULL = Enum.INT_NULL;
    int INT_ABOVE_AVERAGE = Enum.INT_ABOVE_AVERAGE;
    int INT_BELOW_AVERAGE = Enum.INT_BELOW_AVERAGE;
    int INT_TOMORROW = Enum.INT_TOMORROW;
    int INT_TODAY = Enum.INT_TODAY;
    int INT_YESTERDAY = Enum.INT_YESTERDAY;
    int INT_NEXT_WEEK = Enum.INT_NEXT_WEEK;
    int INT_THIS_WEEK = Enum.INT_THIS_WEEK;
    int INT_LAST_WEEK = Enum.INT_LAST_WEEK;
    int INT_NEXT_MONTH = Enum.INT_NEXT_MONTH;
    int INT_THIS_MONTH = Enum.INT_THIS_MONTH;
    int INT_LAST_MONTH = Enum.INT_LAST_MONTH;
    int INT_NEXT_QUARTER = Enum.INT_NEXT_QUARTER;
    int INT_THIS_QUARTER = Enum.INT_THIS_QUARTER;
    int INT_LAST_QUARTER = Enum.INT_LAST_QUARTER;
    int INT_NEXT_YEAR = Enum.INT_NEXT_YEAR;
    int INT_THIS_YEAR = Enum.INT_THIS_YEAR;
    int INT_LAST_YEAR = Enum.INT_LAST_YEAR;
    int INT_YEAR_TO_DATE = Enum.INT_YEAR_TO_DATE;
    int INT_Q_1 = Enum.INT_Q_1;
    int INT_Q_2 = Enum.INT_Q_2;
    int INT_Q_3 = Enum.INT_Q_3;
    int INT_Q_4 = Enum.INT_Q_4;
    int INT_M_1 = Enum.INT_M_1;
    int INT_M_2 = Enum.INT_M_2;
    int INT_M_3 = Enum.INT_M_3;
    int INT_M_4 = Enum.INT_M_4;
    int INT_M_5 = Enum.INT_M_5;
    int INT_M_6 = Enum.INT_M_6;
    int INT_M_7 = Enum.INT_M_7;
    int INT_M_8 = Enum.INT_M_8;
    int INT_M_9 = Enum.INT_M_9;
    int INT_M_10 = Enum.INT_M_10;
    int INT_M_11 = Enum.INT_M_11;
    int INT_M_12 = Enum.INT_M_12;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STDynamicFilterType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NULL
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

        static final int INT_NULL = 1;
        static final int INT_ABOVE_AVERAGE = 2;
        static final int INT_BELOW_AVERAGE = 3;
        static final int INT_TOMORROW = 4;
        static final int INT_TODAY = 5;
        static final int INT_YESTERDAY = 6;
        static final int INT_NEXT_WEEK = 7;
        static final int INT_THIS_WEEK = 8;
        static final int INT_LAST_WEEK = 9;
        static final int INT_NEXT_MONTH = 10;
        static final int INT_THIS_MONTH = 11;
        static final int INT_LAST_MONTH = 12;
        static final int INT_NEXT_QUARTER = 13;
        static final int INT_THIS_QUARTER = 14;
        static final int INT_LAST_QUARTER = 15;
        static final int INT_NEXT_YEAR = 16;
        static final int INT_THIS_YEAR = 17;
        static final int INT_LAST_YEAR = 18;
        static final int INT_YEAR_TO_DATE = 19;
        static final int INT_Q_1 = 20;
        static final int INT_Q_2 = 21;
        static final int INT_Q_3 = 22;
        static final int INT_Q_4 = 23;
        static final int INT_M_1 = 24;
        static final int INT_M_2 = 25;
        static final int INT_M_3 = 26;
        static final int INT_M_4 = 27;
        static final int INT_M_5 = 28;
        static final int INT_M_6 = 29;
        static final int INT_M_7 = 30;
        static final int INT_M_8 = 31;
        static final int INT_M_9 = 32;
        static final int INT_M_10 = 33;
        static final int INT_M_11 = 34;
        static final int INT_M_12 = 35;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("null", INT_NULL),
            new Enum("aboveAverage", INT_ABOVE_AVERAGE),
            new Enum("belowAverage", INT_BELOW_AVERAGE),
            new Enum("tomorrow", INT_TOMORROW),
            new Enum("today", INT_TODAY),
            new Enum("yesterday", INT_YESTERDAY),
            new Enum("nextWeek", INT_NEXT_WEEK),
            new Enum("thisWeek", INT_THIS_WEEK),
            new Enum("lastWeek", INT_LAST_WEEK),
            new Enum("nextMonth", INT_NEXT_MONTH),
            new Enum("thisMonth", INT_THIS_MONTH),
            new Enum("lastMonth", INT_LAST_MONTH),
            new Enum("nextQuarter", INT_NEXT_QUARTER),
            new Enum("thisQuarter", INT_THIS_QUARTER),
            new Enum("lastQuarter", INT_LAST_QUARTER),
            new Enum("nextYear", INT_NEXT_YEAR),
            new Enum("thisYear", INT_THIS_YEAR),
            new Enum("lastYear", INT_LAST_YEAR),
            new Enum("yearToDate", INT_YEAR_TO_DATE),
            new Enum("Q1", INT_Q_1),
            new Enum("Q2", INT_Q_2),
            new Enum("Q3", INT_Q_3),
            new Enum("Q4", INT_Q_4),
            new Enum("M1", INT_M_1),
            new Enum("M2", INT_M_2),
            new Enum("M3", INT_M_3),
            new Enum("M4", INT_M_4),
            new Enum("M5", INT_M_5),
            new Enum("M6", INT_M_6),
            new Enum("M7", INT_M_7),
            new Enum("M8", INT_M_8),
            new Enum("M9", INT_M_9),
            new Enum("M10", INT_M_10),
            new Enum("M11", INT_M_11),
            new Enum("M12", INT_M_12),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
