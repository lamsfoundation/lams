/*
 * XML Type:  ST_GroupBy
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STGroupBy
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_GroupBy(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STGroupBy.
 */
public interface STGroupBy extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STGroupBy> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stgroupbya405type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum RANGE = Enum.forString("range");
    Enum SECONDS = Enum.forString("seconds");
    Enum MINUTES = Enum.forString("minutes");
    Enum HOURS = Enum.forString("hours");
    Enum DAYS = Enum.forString("days");
    Enum MONTHS = Enum.forString("months");
    Enum QUARTERS = Enum.forString("quarters");
    Enum YEARS = Enum.forString("years");

    int INT_RANGE = Enum.INT_RANGE;
    int INT_SECONDS = Enum.INT_SECONDS;
    int INT_MINUTES = Enum.INT_MINUTES;
    int INT_HOURS = Enum.INT_HOURS;
    int INT_DAYS = Enum.INT_DAYS;
    int INT_MONTHS = Enum.INT_MONTHS;
    int INT_QUARTERS = Enum.INT_QUARTERS;
    int INT_YEARS = Enum.INT_YEARS;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STGroupBy.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_RANGE
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

        static final int INT_RANGE = 1;
        static final int INT_SECONDS = 2;
        static final int INT_MINUTES = 3;
        static final int INT_HOURS = 4;
        static final int INT_DAYS = 5;
        static final int INT_MONTHS = 6;
        static final int INT_QUARTERS = 7;
        static final int INT_YEARS = 8;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("range", INT_RANGE),
            new Enum("seconds", INT_SECONDS),
            new Enum("minutes", INT_MINUTES),
            new Enum("hours", INT_HOURS),
            new Enum("days", INT_DAYS),
            new Enum("months", INT_MONTHS),
            new Enum("quarters", INT_QUARTERS),
            new Enum("years", INT_YEARS),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
