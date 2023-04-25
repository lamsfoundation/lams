/*
 * XML Type:  ST_DataValidationType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_DataValidationType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType.
 */
public interface STDataValidationType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stdatavalidationtypeabf6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NONE = Enum.forString("none");
    Enum WHOLE = Enum.forString("whole");
    Enum DECIMAL = Enum.forString("decimal");
    Enum LIST = Enum.forString("list");
    Enum DATE = Enum.forString("date");
    Enum TIME = Enum.forString("time");
    Enum TEXT_LENGTH = Enum.forString("textLength");
    Enum CUSTOM = Enum.forString("custom");

    int INT_NONE = Enum.INT_NONE;
    int INT_WHOLE = Enum.INT_WHOLE;
    int INT_DECIMAL = Enum.INT_DECIMAL;
    int INT_LIST = Enum.INT_LIST;
    int INT_DATE = Enum.INT_DATE;
    int INT_TIME = Enum.INT_TIME;
    int INT_TEXT_LENGTH = Enum.INT_TEXT_LENGTH;
    int INT_CUSTOM = Enum.INT_CUSTOM;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NONE
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

        static final int INT_NONE = 1;
        static final int INT_WHOLE = 2;
        static final int INT_DECIMAL = 3;
        static final int INT_LIST = 4;
        static final int INT_DATE = 5;
        static final int INT_TIME = 6;
        static final int INT_TEXT_LENGTH = 7;
        static final int INT_CUSTOM = 8;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("none", INT_NONE),
            new Enum("whole", INT_WHOLE),
            new Enum("decimal", INT_DECIMAL),
            new Enum("list", INT_LIST),
            new Enum("date", INT_DATE),
            new Enum("time", INT_TIME),
            new Enum("textLength", INT_TEXT_LENGTH),
            new Enum("custom", INT_CUSTOM),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
