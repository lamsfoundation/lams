/*
 * XML Type:  ST_DateTimeGrouping
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_DateTimeGrouping(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping.
 */
public interface STDateTimeGrouping extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stdatetimegrouping6e17type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum YEAR = Enum.forString("year");
    Enum MONTH = Enum.forString("month");
    Enum DAY = Enum.forString("day");
    Enum HOUR = Enum.forString("hour");
    Enum MINUTE = Enum.forString("minute");
    Enum SECOND = Enum.forString("second");

    int INT_YEAR = Enum.INT_YEAR;
    int INT_MONTH = Enum.INT_MONTH;
    int INT_DAY = Enum.INT_DAY;
    int INT_HOUR = Enum.INT_HOUR;
    int INT_MINUTE = Enum.INT_MINUTE;
    int INT_SECOND = Enum.INT_SECOND;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STDateTimeGrouping.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_YEAR
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

        static final int INT_YEAR = 1;
        static final int INT_MONTH = 2;
        static final int INT_DAY = 3;
        static final int INT_HOUR = 4;
        static final int INT_MINUTE = 5;
        static final int INT_SECOND = 6;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("year", INT_YEAR),
            new Enum("month", INT_MONTH),
            new Enum("day", INT_DAY),
            new Enum("hour", INT_HOUR),
            new Enum("minute", INT_MINUTE),
            new Enum("second", INT_SECOND),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
