/*
 * XML Type:  ST_PatternType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STPatternType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_PatternType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STPatternType.
 */
public interface STPatternType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STPatternType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stpatterntype7939type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NONE = Enum.forString("none");
    Enum SOLID = Enum.forString("solid");
    Enum MEDIUM_GRAY = Enum.forString("mediumGray");
    Enum DARK_GRAY = Enum.forString("darkGray");
    Enum LIGHT_GRAY = Enum.forString("lightGray");
    Enum DARK_HORIZONTAL = Enum.forString("darkHorizontal");
    Enum DARK_VERTICAL = Enum.forString("darkVertical");
    Enum DARK_DOWN = Enum.forString("darkDown");
    Enum DARK_UP = Enum.forString("darkUp");
    Enum DARK_GRID = Enum.forString("darkGrid");
    Enum DARK_TRELLIS = Enum.forString("darkTrellis");
    Enum LIGHT_HORIZONTAL = Enum.forString("lightHorizontal");
    Enum LIGHT_VERTICAL = Enum.forString("lightVertical");
    Enum LIGHT_DOWN = Enum.forString("lightDown");
    Enum LIGHT_UP = Enum.forString("lightUp");
    Enum LIGHT_GRID = Enum.forString("lightGrid");
    Enum LIGHT_TRELLIS = Enum.forString("lightTrellis");
    Enum GRAY_125 = Enum.forString("gray125");
    Enum GRAY_0625 = Enum.forString("gray0625");

    int INT_NONE = Enum.INT_NONE;
    int INT_SOLID = Enum.INT_SOLID;
    int INT_MEDIUM_GRAY = Enum.INT_MEDIUM_GRAY;
    int INT_DARK_GRAY = Enum.INT_DARK_GRAY;
    int INT_LIGHT_GRAY = Enum.INT_LIGHT_GRAY;
    int INT_DARK_HORIZONTAL = Enum.INT_DARK_HORIZONTAL;
    int INT_DARK_VERTICAL = Enum.INT_DARK_VERTICAL;
    int INT_DARK_DOWN = Enum.INT_DARK_DOWN;
    int INT_DARK_UP = Enum.INT_DARK_UP;
    int INT_DARK_GRID = Enum.INT_DARK_GRID;
    int INT_DARK_TRELLIS = Enum.INT_DARK_TRELLIS;
    int INT_LIGHT_HORIZONTAL = Enum.INT_LIGHT_HORIZONTAL;
    int INT_LIGHT_VERTICAL = Enum.INT_LIGHT_VERTICAL;
    int INT_LIGHT_DOWN = Enum.INT_LIGHT_DOWN;
    int INT_LIGHT_UP = Enum.INT_LIGHT_UP;
    int INT_LIGHT_GRID = Enum.INT_LIGHT_GRID;
    int INT_LIGHT_TRELLIS = Enum.INT_LIGHT_TRELLIS;
    int INT_GRAY_125 = Enum.INT_GRAY_125;
    int INT_GRAY_0625 = Enum.INT_GRAY_0625;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STPatternType.
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
        static final int INT_SOLID = 2;
        static final int INT_MEDIUM_GRAY = 3;
        static final int INT_DARK_GRAY = 4;
        static final int INT_LIGHT_GRAY = 5;
        static final int INT_DARK_HORIZONTAL = 6;
        static final int INT_DARK_VERTICAL = 7;
        static final int INT_DARK_DOWN = 8;
        static final int INT_DARK_UP = 9;
        static final int INT_DARK_GRID = 10;
        static final int INT_DARK_TRELLIS = 11;
        static final int INT_LIGHT_HORIZONTAL = 12;
        static final int INT_LIGHT_VERTICAL = 13;
        static final int INT_LIGHT_DOWN = 14;
        static final int INT_LIGHT_UP = 15;
        static final int INT_LIGHT_GRID = 16;
        static final int INT_LIGHT_TRELLIS = 17;
        static final int INT_GRAY_125 = 18;
        static final int INT_GRAY_0625 = 19;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("none", INT_NONE),
            new Enum("solid", INT_SOLID),
            new Enum("mediumGray", INT_MEDIUM_GRAY),
            new Enum("darkGray", INT_DARK_GRAY),
            new Enum("lightGray", INT_LIGHT_GRAY),
            new Enum("darkHorizontal", INT_DARK_HORIZONTAL),
            new Enum("darkVertical", INT_DARK_VERTICAL),
            new Enum("darkDown", INT_DARK_DOWN),
            new Enum("darkUp", INT_DARK_UP),
            new Enum("darkGrid", INT_DARK_GRID),
            new Enum("darkTrellis", INT_DARK_TRELLIS),
            new Enum("lightHorizontal", INT_LIGHT_HORIZONTAL),
            new Enum("lightVertical", INT_LIGHT_VERTICAL),
            new Enum("lightDown", INT_LIGHT_DOWN),
            new Enum("lightUp", INT_LIGHT_UP),
            new Enum("lightGrid", INT_LIGHT_GRID),
            new Enum("lightTrellis", INT_LIGHT_TRELLIS),
            new Enum("gray125", INT_GRAY_125),
            new Enum("gray0625", INT_GRAY_0625),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
