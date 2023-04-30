/*
 * XML Type:  ST_PivotAreaType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STPivotAreaType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_PivotAreaType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STPivotAreaType.
 */
public interface STPivotAreaType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STPivotAreaType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stpivotareatypef298type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NONE = Enum.forString("none");
    Enum NORMAL = Enum.forString("normal");
    Enum DATA = Enum.forString("data");
    Enum ALL = Enum.forString("all");
    Enum ORIGIN = Enum.forString("origin");
    Enum BUTTON = Enum.forString("button");
    Enum TOP_END = Enum.forString("topEnd");
    Enum TOP_RIGHT = Enum.forString("topRight");

    int INT_NONE = Enum.INT_NONE;
    int INT_NORMAL = Enum.INT_NORMAL;
    int INT_DATA = Enum.INT_DATA;
    int INT_ALL = Enum.INT_ALL;
    int INT_ORIGIN = Enum.INT_ORIGIN;
    int INT_BUTTON = Enum.INT_BUTTON;
    int INT_TOP_END = Enum.INT_TOP_END;
    int INT_TOP_RIGHT = Enum.INT_TOP_RIGHT;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STPivotAreaType.
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
        static final int INT_NORMAL = 2;
        static final int INT_DATA = 3;
        static final int INT_ALL = 4;
        static final int INT_ORIGIN = 5;
        static final int INT_BUTTON = 6;
        static final int INT_TOP_END = 7;
        static final int INT_TOP_RIGHT = 8;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("none", INT_NONE),
            new Enum("normal", INT_NORMAL),
            new Enum("data", INT_DATA),
            new Enum("all", INT_ALL),
            new Enum("origin", INT_ORIGIN),
            new Enum("button", INT_BUTTON),
            new Enum("topEnd", INT_TOP_END),
            new Enum("topRight", INT_TOP_RIGHT),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
