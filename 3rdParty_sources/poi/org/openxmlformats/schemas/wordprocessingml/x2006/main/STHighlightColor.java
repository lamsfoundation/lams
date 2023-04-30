/*
 * XML Type:  ST_HighlightColor
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_HighlightColor(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor.
 */
public interface STHighlightColor extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sthighlightcolora8e9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum BLACK = Enum.forString("black");
    Enum BLUE = Enum.forString("blue");
    Enum CYAN = Enum.forString("cyan");
    Enum GREEN = Enum.forString("green");
    Enum MAGENTA = Enum.forString("magenta");
    Enum RED = Enum.forString("red");
    Enum YELLOW = Enum.forString("yellow");
    Enum WHITE = Enum.forString("white");
    Enum DARK_BLUE = Enum.forString("darkBlue");
    Enum DARK_CYAN = Enum.forString("darkCyan");
    Enum DARK_GREEN = Enum.forString("darkGreen");
    Enum DARK_MAGENTA = Enum.forString("darkMagenta");
    Enum DARK_RED = Enum.forString("darkRed");
    Enum DARK_YELLOW = Enum.forString("darkYellow");
    Enum DARK_GRAY = Enum.forString("darkGray");
    Enum LIGHT_GRAY = Enum.forString("lightGray");
    Enum NONE = Enum.forString("none");

    int INT_BLACK = Enum.INT_BLACK;
    int INT_BLUE = Enum.INT_BLUE;
    int INT_CYAN = Enum.INT_CYAN;
    int INT_GREEN = Enum.INT_GREEN;
    int INT_MAGENTA = Enum.INT_MAGENTA;
    int INT_RED = Enum.INT_RED;
    int INT_YELLOW = Enum.INT_YELLOW;
    int INT_WHITE = Enum.INT_WHITE;
    int INT_DARK_BLUE = Enum.INT_DARK_BLUE;
    int INT_DARK_CYAN = Enum.INT_DARK_CYAN;
    int INT_DARK_GREEN = Enum.INT_DARK_GREEN;
    int INT_DARK_MAGENTA = Enum.INT_DARK_MAGENTA;
    int INT_DARK_RED = Enum.INT_DARK_RED;
    int INT_DARK_YELLOW = Enum.INT_DARK_YELLOW;
    int INT_DARK_GRAY = Enum.INT_DARK_GRAY;
    int INT_LIGHT_GRAY = Enum.INT_LIGHT_GRAY;
    int INT_NONE = Enum.INT_NONE;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_BLACK
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

        static final int INT_BLACK = 1;
        static final int INT_BLUE = 2;
        static final int INT_CYAN = 3;
        static final int INT_GREEN = 4;
        static final int INT_MAGENTA = 5;
        static final int INT_RED = 6;
        static final int INT_YELLOW = 7;
        static final int INT_WHITE = 8;
        static final int INT_DARK_BLUE = 9;
        static final int INT_DARK_CYAN = 10;
        static final int INT_DARK_GREEN = 11;
        static final int INT_DARK_MAGENTA = 12;
        static final int INT_DARK_RED = 13;
        static final int INT_DARK_YELLOW = 14;
        static final int INT_DARK_GRAY = 15;
        static final int INT_LIGHT_GRAY = 16;
        static final int INT_NONE = 17;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("black", INT_BLACK),
            new Enum("blue", INT_BLUE),
            new Enum("cyan", INT_CYAN),
            new Enum("green", INT_GREEN),
            new Enum("magenta", INT_MAGENTA),
            new Enum("red", INT_RED),
            new Enum("yellow", INT_YELLOW),
            new Enum("white", INT_WHITE),
            new Enum("darkBlue", INT_DARK_BLUE),
            new Enum("darkCyan", INT_DARK_CYAN),
            new Enum("darkGreen", INT_DARK_GREEN),
            new Enum("darkMagenta", INT_DARK_MAGENTA),
            new Enum("darkRed", INT_DARK_RED),
            new Enum("darkYellow", INT_DARK_YELLOW),
            new Enum("darkGray", INT_DARK_GRAY),
            new Enum("lightGray", INT_LIGHT_GRAY),
            new Enum("none", INT_NONE),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
