/*
 * XML Type:  ST_TabJc
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TabJc(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc.
 */
public interface STTabJc extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttabjc10f4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum CLEAR = Enum.forString("clear");
    Enum START = Enum.forString("start");
    Enum CENTER = Enum.forString("center");
    Enum END = Enum.forString("end");
    Enum DECIMAL = Enum.forString("decimal");
    Enum BAR = Enum.forString("bar");
    Enum NUM = Enum.forString("num");
    Enum LEFT = Enum.forString("left");
    Enum RIGHT = Enum.forString("right");

    int INT_CLEAR = Enum.INT_CLEAR;
    int INT_START = Enum.INT_START;
    int INT_CENTER = Enum.INT_CENTER;
    int INT_END = Enum.INT_END;
    int INT_DECIMAL = Enum.INT_DECIMAL;
    int INT_BAR = Enum.INT_BAR;
    int INT_NUM = Enum.INT_NUM;
    int INT_LEFT = Enum.INT_LEFT;
    int INT_RIGHT = Enum.INT_RIGHT;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_CLEAR
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

        static final int INT_CLEAR = 1;
        static final int INT_START = 2;
        static final int INT_CENTER = 3;
        static final int INT_END = 4;
        static final int INT_DECIMAL = 5;
        static final int INT_BAR = 6;
        static final int INT_NUM = 7;
        static final int INT_LEFT = 8;
        static final int INT_RIGHT = 9;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("clear", INT_CLEAR),
            new Enum("start", INT_START),
            new Enum("center", INT_CENTER),
            new Enum("end", INT_END),
            new Enum("decimal", INT_DECIMAL),
            new Enum("bar", INT_BAR),
            new Enum("num", INT_NUM),
            new Enum("left", INT_LEFT),
            new Enum("right", INT_RIGHT),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
