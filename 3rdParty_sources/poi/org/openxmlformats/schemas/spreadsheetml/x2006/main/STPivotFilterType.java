/*
 * XML Type:  ST_PivotFilterType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STPivotFilterType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_PivotFilterType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STPivotFilterType.
 */
public interface STPivotFilterType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STPivotFilterType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stpivotfiltertype4503type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum UNKNOWN = Enum.forString("unknown");
    Enum COUNT = Enum.forString("count");
    Enum PERCENT = Enum.forString("percent");
    Enum SUM = Enum.forString("sum");
    Enum CAPTION_EQUAL = Enum.forString("captionEqual");
    Enum CAPTION_NOT_EQUAL = Enum.forString("captionNotEqual");
    Enum CAPTION_BEGINS_WITH = Enum.forString("captionBeginsWith");
    Enum CAPTION_NOT_BEGINS_WITH = Enum.forString("captionNotBeginsWith");
    Enum CAPTION_ENDS_WITH = Enum.forString("captionEndsWith");
    Enum CAPTION_NOT_ENDS_WITH = Enum.forString("captionNotEndsWith");
    Enum CAPTION_CONTAINS = Enum.forString("captionContains");
    Enum CAPTION_NOT_CONTAINS = Enum.forString("captionNotContains");
    Enum CAPTION_GREATER_THAN = Enum.forString("captionGreaterThan");
    Enum CAPTION_GREATER_THAN_OR_EQUAL = Enum.forString("captionGreaterThanOrEqual");
    Enum CAPTION_LESS_THAN = Enum.forString("captionLessThan");
    Enum CAPTION_LESS_THAN_OR_EQUAL = Enum.forString("captionLessThanOrEqual");
    Enum CAPTION_BETWEEN = Enum.forString("captionBetween");
    Enum CAPTION_NOT_BETWEEN = Enum.forString("captionNotBetween");
    Enum VALUE_EQUAL = Enum.forString("valueEqual");
    Enum VALUE_NOT_EQUAL = Enum.forString("valueNotEqual");
    Enum VALUE_GREATER_THAN = Enum.forString("valueGreaterThan");
    Enum VALUE_GREATER_THAN_OR_EQUAL = Enum.forString("valueGreaterThanOrEqual");
    Enum VALUE_LESS_THAN = Enum.forString("valueLessThan");
    Enum VALUE_LESS_THAN_OR_EQUAL = Enum.forString("valueLessThanOrEqual");
    Enum VALUE_BETWEEN = Enum.forString("valueBetween");
    Enum VALUE_NOT_BETWEEN = Enum.forString("valueNotBetween");
    Enum DATE_EQUAL = Enum.forString("dateEqual");
    Enum DATE_NOT_EQUAL = Enum.forString("dateNotEqual");
    Enum DATE_OLDER_THAN = Enum.forString("dateOlderThan");
    Enum DATE_OLDER_THAN_OR_EQUAL = Enum.forString("dateOlderThanOrEqual");
    Enum DATE_NEWER_THAN = Enum.forString("dateNewerThan");
    Enum DATE_NEWER_THAN_OR_EQUAL = Enum.forString("dateNewerThanOrEqual");
    Enum DATE_BETWEEN = Enum.forString("dateBetween");
    Enum DATE_NOT_BETWEEN = Enum.forString("dateNotBetween");
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

    int INT_UNKNOWN = Enum.INT_UNKNOWN;
    int INT_COUNT = Enum.INT_COUNT;
    int INT_PERCENT = Enum.INT_PERCENT;
    int INT_SUM = Enum.INT_SUM;
    int INT_CAPTION_EQUAL = Enum.INT_CAPTION_EQUAL;
    int INT_CAPTION_NOT_EQUAL = Enum.INT_CAPTION_NOT_EQUAL;
    int INT_CAPTION_BEGINS_WITH = Enum.INT_CAPTION_BEGINS_WITH;
    int INT_CAPTION_NOT_BEGINS_WITH = Enum.INT_CAPTION_NOT_BEGINS_WITH;
    int INT_CAPTION_ENDS_WITH = Enum.INT_CAPTION_ENDS_WITH;
    int INT_CAPTION_NOT_ENDS_WITH = Enum.INT_CAPTION_NOT_ENDS_WITH;
    int INT_CAPTION_CONTAINS = Enum.INT_CAPTION_CONTAINS;
    int INT_CAPTION_NOT_CONTAINS = Enum.INT_CAPTION_NOT_CONTAINS;
    int INT_CAPTION_GREATER_THAN = Enum.INT_CAPTION_GREATER_THAN;
    int INT_CAPTION_GREATER_THAN_OR_EQUAL = Enum.INT_CAPTION_GREATER_THAN_OR_EQUAL;
    int INT_CAPTION_LESS_THAN = Enum.INT_CAPTION_LESS_THAN;
    int INT_CAPTION_LESS_THAN_OR_EQUAL = Enum.INT_CAPTION_LESS_THAN_OR_EQUAL;
    int INT_CAPTION_BETWEEN = Enum.INT_CAPTION_BETWEEN;
    int INT_CAPTION_NOT_BETWEEN = Enum.INT_CAPTION_NOT_BETWEEN;
    int INT_VALUE_EQUAL = Enum.INT_VALUE_EQUAL;
    int INT_VALUE_NOT_EQUAL = Enum.INT_VALUE_NOT_EQUAL;
    int INT_VALUE_GREATER_THAN = Enum.INT_VALUE_GREATER_THAN;
    int INT_VALUE_GREATER_THAN_OR_EQUAL = Enum.INT_VALUE_GREATER_THAN_OR_EQUAL;
    int INT_VALUE_LESS_THAN = Enum.INT_VALUE_LESS_THAN;
    int INT_VALUE_LESS_THAN_OR_EQUAL = Enum.INT_VALUE_LESS_THAN_OR_EQUAL;
    int INT_VALUE_BETWEEN = Enum.INT_VALUE_BETWEEN;
    int INT_VALUE_NOT_BETWEEN = Enum.INT_VALUE_NOT_BETWEEN;
    int INT_DATE_EQUAL = Enum.INT_DATE_EQUAL;
    int INT_DATE_NOT_EQUAL = Enum.INT_DATE_NOT_EQUAL;
    int INT_DATE_OLDER_THAN = Enum.INT_DATE_OLDER_THAN;
    int INT_DATE_OLDER_THAN_OR_EQUAL = Enum.INT_DATE_OLDER_THAN_OR_EQUAL;
    int INT_DATE_NEWER_THAN = Enum.INT_DATE_NEWER_THAN;
    int INT_DATE_NEWER_THAN_OR_EQUAL = Enum.INT_DATE_NEWER_THAN_OR_EQUAL;
    int INT_DATE_BETWEEN = Enum.INT_DATE_BETWEEN;
    int INT_DATE_NOT_BETWEEN = Enum.INT_DATE_NOT_BETWEEN;
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
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STPivotFilterType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_UNKNOWN
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

        static final int INT_UNKNOWN = 1;
        static final int INT_COUNT = 2;
        static final int INT_PERCENT = 3;
        static final int INT_SUM = 4;
        static final int INT_CAPTION_EQUAL = 5;
        static final int INT_CAPTION_NOT_EQUAL = 6;
        static final int INT_CAPTION_BEGINS_WITH = 7;
        static final int INT_CAPTION_NOT_BEGINS_WITH = 8;
        static final int INT_CAPTION_ENDS_WITH = 9;
        static final int INT_CAPTION_NOT_ENDS_WITH = 10;
        static final int INT_CAPTION_CONTAINS = 11;
        static final int INT_CAPTION_NOT_CONTAINS = 12;
        static final int INT_CAPTION_GREATER_THAN = 13;
        static final int INT_CAPTION_GREATER_THAN_OR_EQUAL = 14;
        static final int INT_CAPTION_LESS_THAN = 15;
        static final int INT_CAPTION_LESS_THAN_OR_EQUAL = 16;
        static final int INT_CAPTION_BETWEEN = 17;
        static final int INT_CAPTION_NOT_BETWEEN = 18;
        static final int INT_VALUE_EQUAL = 19;
        static final int INT_VALUE_NOT_EQUAL = 20;
        static final int INT_VALUE_GREATER_THAN = 21;
        static final int INT_VALUE_GREATER_THAN_OR_EQUAL = 22;
        static final int INT_VALUE_LESS_THAN = 23;
        static final int INT_VALUE_LESS_THAN_OR_EQUAL = 24;
        static final int INT_VALUE_BETWEEN = 25;
        static final int INT_VALUE_NOT_BETWEEN = 26;
        static final int INT_DATE_EQUAL = 27;
        static final int INT_DATE_NOT_EQUAL = 28;
        static final int INT_DATE_OLDER_THAN = 29;
        static final int INT_DATE_OLDER_THAN_OR_EQUAL = 30;
        static final int INT_DATE_NEWER_THAN = 31;
        static final int INT_DATE_NEWER_THAN_OR_EQUAL = 32;
        static final int INT_DATE_BETWEEN = 33;
        static final int INT_DATE_NOT_BETWEEN = 34;
        static final int INT_TOMORROW = 35;
        static final int INT_TODAY = 36;
        static final int INT_YESTERDAY = 37;
        static final int INT_NEXT_WEEK = 38;
        static final int INT_THIS_WEEK = 39;
        static final int INT_LAST_WEEK = 40;
        static final int INT_NEXT_MONTH = 41;
        static final int INT_THIS_MONTH = 42;
        static final int INT_LAST_MONTH = 43;
        static final int INT_NEXT_QUARTER = 44;
        static final int INT_THIS_QUARTER = 45;
        static final int INT_LAST_QUARTER = 46;
        static final int INT_NEXT_YEAR = 47;
        static final int INT_THIS_YEAR = 48;
        static final int INT_LAST_YEAR = 49;
        static final int INT_YEAR_TO_DATE = 50;
        static final int INT_Q_1 = 51;
        static final int INT_Q_2 = 52;
        static final int INT_Q_3 = 53;
        static final int INT_Q_4 = 54;
        static final int INT_M_1 = 55;
        static final int INT_M_2 = 56;
        static final int INT_M_3 = 57;
        static final int INT_M_4 = 58;
        static final int INT_M_5 = 59;
        static final int INT_M_6 = 60;
        static final int INT_M_7 = 61;
        static final int INT_M_8 = 62;
        static final int INT_M_9 = 63;
        static final int INT_M_10 = 64;
        static final int INT_M_11 = 65;
        static final int INT_M_12 = 66;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("unknown", INT_UNKNOWN),
            new Enum("count", INT_COUNT),
            new Enum("percent", INT_PERCENT),
            new Enum("sum", INT_SUM),
            new Enum("captionEqual", INT_CAPTION_EQUAL),
            new Enum("captionNotEqual", INT_CAPTION_NOT_EQUAL),
            new Enum("captionBeginsWith", INT_CAPTION_BEGINS_WITH),
            new Enum("captionNotBeginsWith", INT_CAPTION_NOT_BEGINS_WITH),
            new Enum("captionEndsWith", INT_CAPTION_ENDS_WITH),
            new Enum("captionNotEndsWith", INT_CAPTION_NOT_ENDS_WITH),
            new Enum("captionContains", INT_CAPTION_CONTAINS),
            new Enum("captionNotContains", INT_CAPTION_NOT_CONTAINS),
            new Enum("captionGreaterThan", INT_CAPTION_GREATER_THAN),
            new Enum("captionGreaterThanOrEqual", INT_CAPTION_GREATER_THAN_OR_EQUAL),
            new Enum("captionLessThan", INT_CAPTION_LESS_THAN),
            new Enum("captionLessThanOrEqual", INT_CAPTION_LESS_THAN_OR_EQUAL),
            new Enum("captionBetween", INT_CAPTION_BETWEEN),
            new Enum("captionNotBetween", INT_CAPTION_NOT_BETWEEN),
            new Enum("valueEqual", INT_VALUE_EQUAL),
            new Enum("valueNotEqual", INT_VALUE_NOT_EQUAL),
            new Enum("valueGreaterThan", INT_VALUE_GREATER_THAN),
            new Enum("valueGreaterThanOrEqual", INT_VALUE_GREATER_THAN_OR_EQUAL),
            new Enum("valueLessThan", INT_VALUE_LESS_THAN),
            new Enum("valueLessThanOrEqual", INT_VALUE_LESS_THAN_OR_EQUAL),
            new Enum("valueBetween", INT_VALUE_BETWEEN),
            new Enum("valueNotBetween", INT_VALUE_NOT_BETWEEN),
            new Enum("dateEqual", INT_DATE_EQUAL),
            new Enum("dateNotEqual", INT_DATE_NOT_EQUAL),
            new Enum("dateOlderThan", INT_DATE_OLDER_THAN),
            new Enum("dateOlderThanOrEqual", INT_DATE_OLDER_THAN_OR_EQUAL),
            new Enum("dateNewerThan", INT_DATE_NEWER_THAN),
            new Enum("dateNewerThanOrEqual", INT_DATE_NEWER_THAN_OR_EQUAL),
            new Enum("dateBetween", INT_DATE_BETWEEN),
            new Enum("dateNotBetween", INT_DATE_NOT_BETWEEN),
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
