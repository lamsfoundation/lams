/*
 * XML Type:  ST_CfType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_CfType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfType.
 */
public interface STCfType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stcftype8016type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum EXPRESSION = Enum.forString("expression");
    Enum CELL_IS = Enum.forString("cellIs");
    Enum COLOR_SCALE = Enum.forString("colorScale");
    Enum DATA_BAR = Enum.forString("dataBar");
    Enum ICON_SET = Enum.forString("iconSet");
    Enum TOP_10 = Enum.forString("top10");
    Enum UNIQUE_VALUES = Enum.forString("uniqueValues");
    Enum DUPLICATE_VALUES = Enum.forString("duplicateValues");
    Enum CONTAINS_TEXT = Enum.forString("containsText");
    Enum NOT_CONTAINS_TEXT = Enum.forString("notContainsText");
    Enum BEGINS_WITH = Enum.forString("beginsWith");
    Enum ENDS_WITH = Enum.forString("endsWith");
    Enum CONTAINS_BLANKS = Enum.forString("containsBlanks");
    Enum NOT_CONTAINS_BLANKS = Enum.forString("notContainsBlanks");
    Enum CONTAINS_ERRORS = Enum.forString("containsErrors");
    Enum NOT_CONTAINS_ERRORS = Enum.forString("notContainsErrors");
    Enum TIME_PERIOD = Enum.forString("timePeriod");
    Enum ABOVE_AVERAGE = Enum.forString("aboveAverage");

    int INT_EXPRESSION = Enum.INT_EXPRESSION;
    int INT_CELL_IS = Enum.INT_CELL_IS;
    int INT_COLOR_SCALE = Enum.INT_COLOR_SCALE;
    int INT_DATA_BAR = Enum.INT_DATA_BAR;
    int INT_ICON_SET = Enum.INT_ICON_SET;
    int INT_TOP_10 = Enum.INT_TOP_10;
    int INT_UNIQUE_VALUES = Enum.INT_UNIQUE_VALUES;
    int INT_DUPLICATE_VALUES = Enum.INT_DUPLICATE_VALUES;
    int INT_CONTAINS_TEXT = Enum.INT_CONTAINS_TEXT;
    int INT_NOT_CONTAINS_TEXT = Enum.INT_NOT_CONTAINS_TEXT;
    int INT_BEGINS_WITH = Enum.INT_BEGINS_WITH;
    int INT_ENDS_WITH = Enum.INT_ENDS_WITH;
    int INT_CONTAINS_BLANKS = Enum.INT_CONTAINS_BLANKS;
    int INT_NOT_CONTAINS_BLANKS = Enum.INT_NOT_CONTAINS_BLANKS;
    int INT_CONTAINS_ERRORS = Enum.INT_CONTAINS_ERRORS;
    int INT_NOT_CONTAINS_ERRORS = Enum.INT_NOT_CONTAINS_ERRORS;
    int INT_TIME_PERIOD = Enum.INT_TIME_PERIOD;
    int INT_ABOVE_AVERAGE = Enum.INT_ABOVE_AVERAGE;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_EXPRESSION
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

        static final int INT_EXPRESSION = 1;
        static final int INT_CELL_IS = 2;
        static final int INT_COLOR_SCALE = 3;
        static final int INT_DATA_BAR = 4;
        static final int INT_ICON_SET = 5;
        static final int INT_TOP_10 = 6;
        static final int INT_UNIQUE_VALUES = 7;
        static final int INT_DUPLICATE_VALUES = 8;
        static final int INT_CONTAINS_TEXT = 9;
        static final int INT_NOT_CONTAINS_TEXT = 10;
        static final int INT_BEGINS_WITH = 11;
        static final int INT_ENDS_WITH = 12;
        static final int INT_CONTAINS_BLANKS = 13;
        static final int INT_NOT_CONTAINS_BLANKS = 14;
        static final int INT_CONTAINS_ERRORS = 15;
        static final int INT_NOT_CONTAINS_ERRORS = 16;
        static final int INT_TIME_PERIOD = 17;
        static final int INT_ABOVE_AVERAGE = 18;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("expression", INT_EXPRESSION),
            new Enum("cellIs", INT_CELL_IS),
            new Enum("colorScale", INT_COLOR_SCALE),
            new Enum("dataBar", INT_DATA_BAR),
            new Enum("iconSet", INT_ICON_SET),
            new Enum("top10", INT_TOP_10),
            new Enum("uniqueValues", INT_UNIQUE_VALUES),
            new Enum("duplicateValues", INT_DUPLICATE_VALUES),
            new Enum("containsText", INT_CONTAINS_TEXT),
            new Enum("notContainsText", INT_NOT_CONTAINS_TEXT),
            new Enum("beginsWith", INT_BEGINS_WITH),
            new Enum("endsWith", INT_ENDS_WITH),
            new Enum("containsBlanks", INT_CONTAINS_BLANKS),
            new Enum("notContainsBlanks", INT_NOT_CONTAINS_BLANKS),
            new Enum("containsErrors", INT_CONTAINS_ERRORS),
            new Enum("notContainsErrors", INT_NOT_CONTAINS_ERRORS),
            new Enum("timePeriod", INT_TIME_PERIOD),
            new Enum("aboveAverage", INT_ABOVE_AVERAGE),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
